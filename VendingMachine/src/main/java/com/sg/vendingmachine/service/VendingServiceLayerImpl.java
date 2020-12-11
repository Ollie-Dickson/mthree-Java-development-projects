package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingAuditDao;
import com.sg.vendingmachine.dao.VendingDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Coins;
import com.sg.vendingmachine.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendingServiceLayerImpl implements VendingServiceLayer{

    private VendingDao dao;
    private VendingAuditDao auditDao;

    @Autowired
    public VendingServiceLayerImpl(VendingDao dao, VendingAuditDao auditDao){
        this.dao = dao;
        this. auditDao = auditDao;
    }

    @Override
    public void loadInventory() throws VendingMachinePersistenceException{
        dao.loadInventory();
    }

    @Override
    public List<Item> getInventory() throws VendingMachinePersistenceException {
        List<Item> stock = dao.getInventory();
        List<Item> stockedItems = stock.stream()
                .filter((i) -> i.getInventory() > 0) //filter out items with 0 stock
                .collect(Collectors.toList());
        return stockedItems;
    }

    @Override
    public BigDecimal getCash(){
        return dao.getCash();
    }

    @Override
    public void addCash(BigDecimal moneyAdded) throws VendingMachinePersistenceException{
        dao.addCash(moneyAdded);
        auditDao.writeAuditEntry("£"+ moneyAdded +" inserted into machine.  Total money:£"+ dao.getCash());
    }

    @Override
    public BigDecimal purchaseItem(Item item, BigDecimal cash) throws
            VendingMachinePersistenceException, InsufficientFundsException {
        if(item.getCost().compareTo(cash) > 0){
            // if item cost is greater than available cash
            throw new InsufficientFundsException("--INSUFFICIENT FUNDS FOR THIS TRANSACTION--");
        } else {
            dao.purchaseItem(item.getName());
            cash = dao.getCash();
            auditDao.writeAuditEntry(item.getName() +" purchased.  Price:£"+ item.getCost() +
                    "  Money remaining:£"+ cash);
            return cash;
        }
    }

    @Override
    public List<Coins> calculateChange(BigDecimal cash) throws VendingMachinePersistenceException{
        auditDao.writeAuditEntry("Change returned:£"+ cash);
        List<Coins> change = new ArrayList<>();
        BigDecimal twoPound = new BigDecimal("2");
        BigDecimal onePound = new BigDecimal("1");
        BigDecimal fiftyPence = new BigDecimal("0.5");
        BigDecimal twentyPence = new BigDecimal("0.2");
        BigDecimal tenPence = new BigDecimal("0.1");
        BigDecimal fivePence = new BigDecimal("0.05");
        BigDecimal twoPence = new BigDecimal("0.02");
        BigDecimal onePence = new BigDecimal("0.01");
        BigDecimal zero = new BigDecimal("0");

        while(cash.compareTo(zero) > 0){
            if(cash.compareTo(twoPound) >= 0){
                change.add(Coins.TWO_POUND);
                cash = cash.subtract(twoPound);
            } else if (cash.compareTo(onePound) >= 0){
                change.add(Coins.ONE_POUND);
                cash = cash.subtract(onePound);
            } else if (cash.compareTo(fiftyPence) >= 0){
                change.add(Coins.FIFTY_PENCE);
                cash = cash.subtract(fiftyPence);
            } else if (cash.compareTo(twentyPence) >= 0){
                change.add(Coins.TWENTY_PENCE);
                cash = cash.subtract(twentyPence);
            } else if (cash.compareTo(tenPence) >= 0){
                change.add(Coins.TEN_PENCE);
                cash = cash.subtract(tenPence);
            } else if (cash.compareTo(fivePence) >= 0){
                change.add(Coins.FIVE_PENCE);
                cash = cash.subtract(fivePence);
            } else if (cash.compareTo(twoPence) >= 0){
                change.add(Coins.TWO_PENCE);
                cash = cash.subtract(twoPence);
            } else if (cash.compareTo(onePence) >= 0){
                change.add(Coins.ONE_PENCE);
                cash = cash.subtract(onePence);
            }
        }
        return change;
    }


}
