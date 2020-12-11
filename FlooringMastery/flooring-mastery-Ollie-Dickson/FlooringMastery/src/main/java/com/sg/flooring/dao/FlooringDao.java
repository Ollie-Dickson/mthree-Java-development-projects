package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.State;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringDao {

    List<Order> getOrders(LocalDate date) throws FlooringPersistenceException;

    void addOrder(Order order, LocalDate date) throws FlooringPersistenceException;

    void removeOrder(LocalDate date, int orderNumber) throws FlooringPersistenceException;

    List<Product> getProducts() throws FlooringPersistenceException;

    List<State> getStates() throws FlooringPersistenceException;

    int getNewOrderNumber() throws FlooringPersistenceException;

    void exportData(String exportFileName) throws FlooringPersistenceException;

    String getDefaultFileName();
}
