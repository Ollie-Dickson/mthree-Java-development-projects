package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Component
public class VendingDaoFileImpl implements VendingDao{

    private String INVENTORY_FILE;
    public static final String DELIMITER = "::";

    public VendingDaoFileImpl(){
        INVENTORY_FILE = "inventory.txt";
    }

    public VendingDaoFileImpl(String inventoryTextFile){
        INVENTORY_FILE = inventoryTextFile;
    }

    private Map<String, Item> itemInventory = new HashMap<>();
    private BigDecimal cash = new BigDecimal("00.00");


    @Override
    public List<Item> getInventory() throws VendingMachinePersistenceException {
        return new ArrayList(itemInventory.values());
    }

    @Override
    public void purchaseItem(String name) throws VendingMachinePersistenceException {
        Item purchasedItem = itemInventory.get(name);
        int inventory = purchasedItem.getInventory();
        purchasedItem.setInventory(inventory - 1);
        cash = cash.subtract(purchasedItem.getCost());
        writeInventory();
    }

    @Override
    public void loadInventory() throws VendingMachinePersistenceException{
        Scanner sc;

        try{
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader(INVENTORY_FILE)));
        } catch (FileNotFoundException e){
            throw new VendingMachinePersistenceException(
                    "Could not load inventory data into memory,", e);
        }
        String currentLine;
        Item currentItem;
        while(sc.hasNextLine()){
            currentLine = sc.nextLine();
            currentItem = unmarshallItem(currentLine);
            itemInventory.put(currentItem.getName(), currentItem);
        }
    }

    @Override
    public BigDecimal getCash(){
        return cash;
    }

    @Override
    public void addCash(BigDecimal moneyAdded){
        cash = cash.add(moneyAdded);
    }

    private Item unmarshallItem(String itemAsText){
        // File line format split with delimiters '::'
        //
        // | Item Name | Cost | Quantity |
        // |    [0]    | [1]  |   [2]    |
        String[] itemData = itemAsText.split(DELIMITER);
        String itemName = itemData[0];
        BigDecimal itemCost = new BigDecimal(itemData[1]);
        int itemCount = Integer.parseInt(itemData[2]);
        return new Item(itemName, itemCost, itemCount);
    }

    private String marshallItem(Item someItem){
        String itemAsText = someItem.getName() + DELIMITER;
        itemAsText += someItem.getCost() + DELIMITER;
        itemAsText += someItem.getInventory();
        return itemAsText;
    }

    private void writeInventory() throws VendingMachinePersistenceException {
        try(PrintWriter out = new PrintWriter(new FileWriter(INVENTORY_FILE))){
            List<Item> itemList = new ArrayList(itemInventory.values());
            for(Item item : itemList){
                out.println(marshallItem(item));
                out.flush();
            }
        } catch (IOException e){
            throw new VendingMachinePersistenceException(
                    "Inventory data could not be saved to file.", e);
        }

    }
}
