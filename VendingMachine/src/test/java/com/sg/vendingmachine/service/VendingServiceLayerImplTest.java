package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingAuditDao;
import com.sg.vendingmachine.dao.VendingDao;
import com.sg.vendingmachine.dto.Coins;
import com.sg.vendingmachine.dto.Item;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendingServiceLayerImplTest {

    private VendingServiceLayer testService;

    public VendingServiceLayerImplTest() {
        //----MANUAL WIRING SETUP----
//        VendingDao dao = new VendingDaoStubImpl();
//        VendingAuditDao auditDao = new VendingAuditDaoStubImpl();
//
//        testService = new VendingServiceLayerImpl(dao, auditDao);
        //----Spring DI with xml----
//        ApplicationContext ctx =
//                new ClassPathXmlApplicationContext("applicationContext.xml");
        //----Spring DI with Annotations----
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("com.sg.vendingmachine.service");
        ctx.refresh();
        testService = ctx.getBean("vendingServiceLayerImpl", VendingServiceLayerImpl.class);
    }

    @Test
    public void getInventoryTest() throws Exception{
        Item itemClone1 = new Item("Coca-cola", new BigDecimal("1.20"), 1);
        Item itemClone2 = new Item("Raisins", new BigDecimal("0.40"), 0);

        assertEquals(1, testService.getInventory().size());
        assertTrue(testService.getInventory().contains(itemClone1));
        assertFalse(testService.getInventory().contains(itemClone2)); //Raisins should have been filtered out
    }

    @Test
    public void purchaseTest() throws Exception{
        Item itemClone1 = new Item("Coca-cola", new BigDecimal("1.20"), 1);
        assertTrue(testService.getInventory().contains(itemClone1));

        List<Item> stockedItems = testService.getInventory();
        for(Item item : stockedItems){
            if(item.getName() == "Coca-cola"){
                //should throw InsufficientFundsException
                assertThrows(InsufficientFundsException.class, () ->testService.purchaseItem(item,
                        new BigDecimal("1.19")));
            }
        }
        //Item should still be in stock as purchase failed
        assertTrue(testService.getInventory().contains(itemClone1));

        stockedItems = testService.getInventory();
        for(Item item : stockedItems){
            if(item.getName() == "Coca-cola"){
                // should not throw InsufficientFundsException if enough cash is given
                assertDoesNotThrow(() ->testService.purchaseItem(item, new BigDecimal("1.20")));
            }
        }
        // Item should no longer be in stock
        assertFalse(testService.getInventory().contains(itemClone1));
    }

    @Test
    public void calculateChangeTest() throws Exception{
        BigDecimal testCash = new BigDecimal("3.88");
        List<Coins> changeClone = new ArrayList<>();
        changeClone.add(Coins.TWO_POUND);
        changeClone.add(Coins.ONE_POUND);
        changeClone.add(Coins.FIFTY_PENCE);
        changeClone.add(Coins.TWENTY_PENCE);
        changeClone.add(Coins.TEN_PENCE);
        changeClone.add(Coins.FIVE_PENCE);
        changeClone.add(Coins.TWO_PENCE);
        changeClone.add(Coins.ONE_PENCE);

        List<Coins> testChange = testService.calculateChange(testCash);
        assertEquals(changeClone, testChange);
    }

}