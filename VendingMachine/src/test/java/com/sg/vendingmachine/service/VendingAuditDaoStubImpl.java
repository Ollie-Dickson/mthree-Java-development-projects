package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingAuditDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import org.springframework.stereotype.Component;

@Component
public class VendingAuditDaoStubImpl implements VendingAuditDao {
    @Override
    public void writeAuditEntry(String entry) throws VendingMachinePersistenceException {
        //do nothing...
    }
}
