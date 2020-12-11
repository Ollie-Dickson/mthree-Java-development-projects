package com.sg.vendingmachine.dao;

public interface VendingAuditDao {
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException;
}
