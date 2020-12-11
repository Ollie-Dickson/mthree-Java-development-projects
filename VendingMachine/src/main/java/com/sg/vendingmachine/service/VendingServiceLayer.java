package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Coins;
import com.sg.vendingmachine.dto.Item;

import java.math.BigDecimal;
import java.util.List;

public interface VendingServiceLayer {

    void loadInventory() throws VendingMachinePersistenceException;

    List<Item> getInventory() throws VendingMachinePersistenceException;

    BigDecimal getCash();

    void addCash(BigDecimal moneyAdded) throws VendingMachinePersistenceException;

    BigDecimal purchaseItem(Item item, BigDecimal cash) throws
            VendingMachinePersistenceException,
            InsufficientFundsException;

    List<Coins> calculateChange(BigDecimal cash) throws VendingMachinePersistenceException;
}
