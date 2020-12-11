package com.sg.flooring.service;

import com.sg.flooring.dao.FlooringDao;
import com.sg.flooring.dao.FlooringPersistenceException;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.State;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FlooringDaoStubImpl implements FlooringDao {

    public Order order1;
    public Order order2;
    public State texas;
    public State washington;
    public Product tile;
    public Product wood;
    public Product carpet;
    List<State> states = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    List<Order> orders = new ArrayList<>();

    public FlooringDaoStubImpl() {
        order1 = new Order(1, "Swiper", "WA", new BigDecimal("6.00"), "Wood",
                new BigDecimal("333.30"), new BigDecimal("5.15"), new BigDecimal("4.75"), new BigDecimal("1716.50"),
                new BigDecimal("1583.18"), new BigDecimal("197.98"), new BigDecimal("3497.66"), "Washington");
        order2 = new Order(2, "Boots", "KY", new BigDecimal("6.00"), "Laminate",
                new BigDecimal("101.56"), new BigDecimal("1.75"), new BigDecimal("2.10"), new BigDecimal("177.73"),
                new BigDecimal("213.28"), new BigDecimal("23.46"), new BigDecimal("414.47"), "Kentucky");
        orders.add(order1);
        orders.add(order2);
        texas = new State("TX", "Texas", new BigDecimal("4.45"));
        washington = new State("WA", "Washington", new BigDecimal("9.25"));
        states.add(texas);
        states.add(washington);
        tile = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        wood = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));
        carpet = new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10"));
        products.add(tile);
        products.add(wood);
        products.add(carpet);
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringPersistenceException {
        return orders;
    }

    @Override
    public void addOrder(Order order, LocalDate date) throws FlooringPersistenceException {
        orders.add(order);
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws FlooringPersistenceException {
        for(Order order : orders){
            if(order.getOrderNumber() == orderNumber){
                orders.remove(order);
            }
        }
    }

    @Override
    public List<Product> getProducts() throws FlooringPersistenceException {
        return products;
    }

    @Override
    public List<State> getStates() throws FlooringPersistenceException {
        return states;
    }

    @Override
    public int getNewOrderNumber() throws FlooringPersistenceException {
        int maxOrderNumber = 0;
        for(Order order : orders){
            if(order.getOrderNumber() > maxOrderNumber){
                maxOrderNumber = order.getOrderNumber();
            }
        }
        return maxOrderNumber;
    }

    @Override
    public void exportData(String exportFileName) throws FlooringPersistenceException {

    }

    @Override
    public String getDefaultFileName() {
        return "TestFiles/Backup/ExportTest.txt";
    }
}
