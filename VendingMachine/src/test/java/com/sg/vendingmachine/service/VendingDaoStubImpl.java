package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Item;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class VendingDaoStubImpl implements VendingDao {

    public Item item1;
    public Item item2;
    public BigDecimal cash;

    public VendingDaoStubImpl() {
        item1 = new Item("Coca-cola", new BigDecimal("1.20"), 1);
        item2 = new Item("Raisins", new BigDecimal("0.40"), 0);
        cash = new BigDecimal("0.00");
    }

    @Override
    public List<Item> getInventory() throws VendingMachinePersistenceException {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        return itemList;
    }

    @Override
    public void purchaseItem(String name) throws VendingMachinePersistenceException {
        if(item1.getName() == name){
            item1.setInventory(item1.getInventory()-1);
            cash = cash.subtract(item1.getCost());
        } else if(item2.getName() == name){
            item2.setInventory(item2.getInventory()-1);
            cash = cash.subtract(item2.getCost());
        }
    }

    @Override
    public void loadInventory() throws VendingMachinePersistenceException {
    }

    @Override
    public BigDecimal getCash() {
        return cash;
    }

    @Override
    public void addCash(BigDecimal moneyAdded) {
        cash = cash.add(moneyAdded);
    }
}
