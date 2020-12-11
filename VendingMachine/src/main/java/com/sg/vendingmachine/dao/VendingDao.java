package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Item;

import java.math.BigDecimal;
import java.util.List;

public interface VendingDao {

    List<Item> getInventory() throws VendingMachinePersistenceException;

    void purchaseItem(String name) throws VendingMachinePersistenceException;

    void loadInventory() throws VendingMachinePersistenceException;

    BigDecimal getCash();

    void addCash(BigDecimal moneyAdded);

    //void itemPurchased(String name) throws VendingMachinePersistenceException;


}
