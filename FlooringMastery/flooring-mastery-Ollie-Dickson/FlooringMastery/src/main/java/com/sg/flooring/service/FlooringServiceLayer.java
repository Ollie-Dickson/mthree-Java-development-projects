package com.sg.flooring.service;

import com.sg.flooring.dao.FlooringPersistenceException;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringServiceLayer {

    List<Order> getOrders(LocalDate date) throws FlooringPersistenceException;

    boolean checkStateName(String stateName) throws FlooringPersistenceException;

    List<Product> getProducts() throws FlooringPersistenceException;

    Order createOrder(String customerName, String stateName, String productName, BigDecimal area, int existingOrderNumber)
            throws FlooringPersistenceException;

    void addOrder(Order order, LocalDate date) throws FlooringPersistenceException;

    void removeOrder(LocalDate date, int orderNumber) throws FlooringPersistenceException;

    void exportData(String exportFileName) throws FlooringPersistenceException;

    String getDefaultFileName();
}
