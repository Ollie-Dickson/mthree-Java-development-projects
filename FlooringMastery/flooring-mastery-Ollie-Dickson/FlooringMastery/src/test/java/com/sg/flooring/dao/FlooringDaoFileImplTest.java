package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FlooringDaoFileImplTest {

    FlooringDao testDao;

    @BeforeEach
    void setUp() throws Exception{
        String testDirectory = "TestFiles";
        PrintWriter out = new PrintWriter(new FileWriter(testDirectory + "/Orders/Orders_01012022.txt"));
        out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                "LabourCostPerSquareFoot,MaterialCost,LabourCost,Tax,Total");
        out.println("2,Boots,KY,6.00,Laminate,101.56,1.75,2.10,177.73,213.28,23.46,414.47");
        out.println("6,Dora,TX,4.45,Carpet,224.00,2.25,2.10,504.00,470.40,43.36,1017.76");
        out.flush();
        out.close();

        testDao = new FlooringDaoFileImpl(testDirectory+"/Data/Taxes.txt", testDirectory+"/Data/Products.txt",
                testDirectory+"/Backup/DataExport.txt", testDirectory+"/Orders");
    }

    @AfterEach
    void tearDown() throws Exception {
        File ordersDirectory = new File("TestFiles/Orders");
        File[] ordersFileList = ordersDirectory.listFiles();
        for(File file : ordersFileList){
            file.delete();
        }
        File backupDirectory = new File("TestFiles/Backup");
        File[] backupFileList = ordersDirectory.listFiles();
        for(File file : ordersFileList){
            file.delete();
        }
    }

    @Test
    public void getAddRemoveOrdersTest() throws Exception {
        Order order2 = new Order(2, "Boots", "KY", new BigDecimal("6.00"), "Laminate",
                new BigDecimal("101.56"), new BigDecimal("1.75"), new BigDecimal("2.10"), new BigDecimal("177.73"),
                new BigDecimal("213.28"), new BigDecimal("23.46"), new BigDecimal("414.47"), "Kentucky");
        Order order6 = new Order(6, "Dora", "TX", new BigDecimal("4.45"), "Carpet",
                new BigDecimal("224.00"), new BigDecimal("2.25"), new BigDecimal("2.10"), new BigDecimal("504.00"),
                new BigDecimal("470.40"), new BigDecimal("43.36"), new BigDecimal("1017.76"), "Texas");

        LocalDate date = LocalDate.parse("01012022", DateTimeFormatter.ofPattern("MMddyyyy"));
        LocalDate wrongDate = LocalDate.parse("02022022", DateTimeFormatter.ofPattern("MMddyyyy"));
        List<Order> orderList = testDao.getOrders(date);
        List<Order> emptyOrderList = testDao.getOrders(wrongDate);

        assertNull(emptyOrderList);

        assertNotNull(orderList);
        assertEquals(2, orderList.size());

        assertTrue(orderList.contains(order2));
        assertTrue(orderList.contains(order6));

        Order order4 = new Order(4, "Swiper", "WA", new BigDecimal("6.00"), "Wood",
                new BigDecimal("333.30"), new BigDecimal("5.15"), new BigDecimal("4.75"), new BigDecimal("1716.50"),
                new BigDecimal("1583.18"), new BigDecimal("197.98"), new BigDecimal("3497.66"), "Washington");

        testDao.addOrder(order4, date);
        orderList = testDao.getOrders(date);

        assertEquals(3, orderList.size());
        assertTrue(orderList.contains(order4));

        testDao.removeOrder(date, order2.getOrderNumber());
        orderList = testDao.getOrders(date);

        assertEquals(2, orderList.size());
        assertFalse(orderList.contains(order2));
        assertTrue(orderList.contains(order4));
        assertTrue(orderList.contains(order6));
    }

    @Test
    public void getNewOrderNumberTest() throws Exception {
        Order order1 = new Order(1, "Swiper", "WA", new BigDecimal("6.00"), "Wood",
                new BigDecimal("333.30"), new BigDecimal("5.15"), new BigDecimal("4.75"), new BigDecimal("1716.50"),
                new BigDecimal("1583.18"), new BigDecimal("197.98"), new BigDecimal("3497.66"), "Washington");
        Order order8 = new Order(8, "Diego", "KY", new BigDecimal("6.00"), "Carpet",
                new BigDecimal("217.00"), new BigDecimal("2.25"), new BigDecimal("2.10"), new BigDecimal("488.25"),
                new BigDecimal("455.70"), new BigDecimal("56.64"), new BigDecimal("1000.59"), "Kentucky");

        LocalDate date = LocalDate.parse("01022022", DateTimeFormatter.ofPattern("MMddyyyy"));

        int newOrderNumber = testDao.getNewOrderNumber();
        assertEquals(7, newOrderNumber);

        testDao.addOrder(order1, date);
        newOrderNumber = testDao.getNewOrderNumber();
        assertEquals(7, newOrderNumber);

        testDao.addOrder(order8, date);
        newOrderNumber = testDao.getNewOrderNumber();
        assertEquals(9, newOrderNumber);
    }

    @Test
    public void getProductsStatesTest() throws Exception {
        Product wood = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));
        State texas = new State("TX", "Texas", new BigDecimal("4.45"));

        List<Product> products = testDao.getProducts();
        List<State> states = testDao.getStates();

        assertNotNull(products);
        assertNotNull(states);
        assertEquals(4, products.size());
        assertEquals(4, states.size());
        assertTrue(products.contains(wood));
        assertTrue(states.contains(texas));
    }

    @Test
    public void exportTest() throws Exception {
        Order order1 = new Order(1, "Swiper", "WA", new BigDecimal("6.00"), "Wood",
                new BigDecimal("333.30"), new BigDecimal("5.15"), new BigDecimal("4.75"), new BigDecimal("1716.50"),
                new BigDecimal("1583.18"), new BigDecimal("197.98"), new BigDecimal("3497.66"), "Washington");

        LocalDate date = LocalDate.parse("01022022", DateTimeFormatter.ofPattern("MMddyyyy"));
        testDao.addOrder(order1, date);
        String fileName = testDao.getDefaultFileName();
        testDao.exportData(fileName);

        Scanner sc;
        try{
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader("TestFiles/Backup/ExportTest.txt")));
        } catch (FileNotFoundException e){
            throw new FlooringPersistenceException(
                    "Could not load data from ExportTest.txt", e);
        }
        String currentLine = sc.nextLine();
        assertTrue(currentLine.equals("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                "LabourCostPerSquareFoot,MaterialCost,LabourCost,Tax,Total,Date"));
        currentLine = sc.nextLine();
        System.out.println(currentLine);
        assertTrue(currentLine.equals("1,Swiper,WA,6.00,Wood,333.30,5.15,4.75,1716.50,1583.18,197.98,3497.66,01-02-2022"));
    }

}