package com.sg.vendingmachine.controller;

import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Coins;
import com.sg.vendingmachine.dto.Item;
import com.sg.vendingmachine.service.InsufficientFundsException;
import com.sg.vendingmachine.service.NoItemInventoryException;
import com.sg.vendingmachine.service.VendingServiceLayer;
import com.sg.vendingmachine.ui.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Controller {
    private View view;
    private VendingServiceLayer service;

    @Autowired
    public Controller(View view, VendingServiceLayer service){
        this.view = view;
        this.service = service;
    }

    public void run(){
        boolean notFinished = true;
        int selectedOption;
        try{
            loadData();
            while(notFinished) {
                List<String> itemNameList = getIndexedList();
                selectedOption = getMenuSelection(itemNameList.size());
                if(selectedOption > 0){ // User selected an item to purchase
                    try{
                        purchaseItem(itemNameList.get(selectedOption - 1));
                    } catch (NoItemInventoryException e){
                        view.displayErrorMessage(e.getMessage());
                    }
                } else if(selectedOption == 0){ // User chose to insert money
                    addMoney();
                } else { // User chose to quit
                    returnChange();
                    notFinished = false;
                }
            }
        } catch (VendingMachinePersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void loadData() throws VendingMachinePersistenceException{
        service.loadInventory();
    }

    /**
     * Method returns a List of stocked item names in the order they appear
     * in the displayed list.
     */
    private List<String> getIndexedList() throws VendingMachinePersistenceException{
        List<Item> stockedItems = service.getInventory();
        BigDecimal cash = service.getCash();
        return view.displayMenu(stockedItems, cash);
    }

    private int getMenuSelection(int itemCount){
        return view.getMenuSelection(itemCount);
    }

    private void purchaseItem(String itemName) throws VendingMachinePersistenceException, NoItemInventoryException{
        List<Item> stockedItems = service.getInventory();
        BigDecimal cash = service.getCash();
        boolean itemFoundInStock = false;
        for(Item item : stockedItems){
            if(item.getName() == itemName){
                itemFoundInStock = true;
                try{
                    cash = service.purchaseItem(item, cash);// returns cash remaining after purchase
                    view.displayPurchaseSuccess(item, cash);
                } catch (InsufficientFundsException e){
                    view.displayErrorMessage(e.getMessage());
                }
            }
        }
        if(!itemFoundInStock){
            throw new NoItemInventoryException("--ITEM NOT IN STOCK--");
        }
    }

    private void addMoney() throws VendingMachinePersistenceException{
        BigDecimal moneyAdded = view.getMoneyAmount();
        service.addCash(moneyAdded);
    }

    private void returnChange() throws VendingMachinePersistenceException{
        BigDecimal cash = service.getCash();
        List<Coins> change = service.calculateChange(cash);
        view.displayReturnedChange(change);
    }

}
