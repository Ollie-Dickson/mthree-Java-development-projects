package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendingDaoFileImplTest {

    VendingDao testDao;

    VendingDaoFileImplTest(){
    }

    @BeforeEach
    void setUp() throws Exception{
        String testFile = "testInventory.txt";
        PrintWriter out = new PrintWriter(new FileWriter(testFile));
        out.println("Walkers Crisps::0.85::2");
        out.println("Coca-cola::1.20::1");
        out.println("Raisins::0.40::0");
        out.flush();
        testDao = new VendingDaoFileImpl(testFile);
        testDao.loadInventory();
    }

    @Test
    public void loadAndGetInventoryTest() throws Exception{
        String itemName = "Walkers Crisps";
        BigDecimal itemCost = new BigDecimal("0.85");
        int itemInventory = 2;
        Item item1 = new Item(itemName, itemCost, itemInventory);
        Item item2 = new Item("Coca-cola", new BigDecimal("1.20"), 1);
        Item item3 = new Item("Raisins", new BigDecimal("0.40"), 0);

        List<Item> retrievedItems = testDao.getInventory();

        assertNotNull(retrievedItems);
        assertEquals(3, retrievedItems.size());

        assertTrue(retrievedItems.contains(item1));
        assertTrue(testDao.getInventory().contains(item2));
        assertTrue(testDao.getInventory().contains(item3));
    }

    @Test
    public void addAndGetCashTest() throws Exception{
        BigDecimal cash = testDao.getCash();
        assertEquals(new BigDecimal("0.00"), cash);

        testDao.addCash(new BigDecimal("1.23"));
        assertEquals(new BigDecimal("1.23"), testDao.getCash());

        testDao.purchaseItem("Coca-cola");
        assertEquals(new BigDecimal("0.03"), testDao.getCash());
    }

    @Test
    public void purchaseItemTest() throws Exception{
        testDao.addCash(new BigDecimal("1.23"));
        Item item = new Item("Coca-cola", new BigDecimal("1.20"), 1);

        assertTrue(testDao.getInventory().contains(item));
        testDao.purchaseItem("Coca-cola");
        assertFalse(testDao.getInventory().contains(item));//should be false due to inventory change

        item.setInventory(0); //set testItem inventory to 0
        assertEquals(new BigDecimal("0.03"), testDao.getCash());
        assertTrue(testDao.getInventory().contains(item));//should now be true

    }

}