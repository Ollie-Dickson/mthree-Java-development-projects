package com.sg.flooring.service;

import com.sg.flooring.dao.FlooringDao;
import com.sg.flooring.dao.FlooringPersistenceException;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringServiceLayerImpl implements FlooringServiceLayer{

    private FlooringDao dao;

    @Autowired
    public FlooringServiceLayerImpl(FlooringDao dao){
        this.dao = dao;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringPersistenceException {
        return dao.getOrders(date);
    }

    @Override
    public boolean checkStateName(String stateName) throws FlooringPersistenceException {
        List<State> stateList = dao.getStates();
        for(State state : stateList){
            if(state.getStateName().toLowerCase().equals(stateName.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Product> getProducts() throws FlooringPersistenceException {
        return dao.getProducts();
    }

    @Override
    public Order createOrder(String customerName, String stateName, String productName, BigDecimal area, int existingOrderNumber)
            throws FlooringPersistenceException{
        int orderNumber;
        if(existingOrderNumber == 0) orderNumber = dao.getNewOrderNumber();
        else orderNumber = existingOrderNumber;
        String stateAbbreviation = "";
        BigDecimal taxRate = new BigDecimal("00.00").setScale(2, RoundingMode.HALF_UP);
        List<State> stateList = dao.getStates();
        for(State state : stateList){
            if(state.getStateName().toLowerCase().equals(stateName.toLowerCase())){
                stateAbbreviation = state.getStateAbbreviation();
                stateName = state.getStateName();
                taxRate = state.getTaxRate();
            }
        }
        BigDecimal costPerSqF = new BigDecimal("00.00");;
        BigDecimal labourCostPerSqF = new BigDecimal("00.00");;
        List<Product> productList = dao.getProducts();
        for(Product product : productList){
            if(product.getProductType().contains(productName)){
                costPerSqF = product.getCostPerSquareFoot();
                labourCostPerSqF = product.getLabourCostPerSquareFoot();
            }
        }
        BigDecimal materialCost = costPerSqF.multiply(area).setScale(2, RoundingMode.HALF_UP);
        BigDecimal labourCost = labourCostPerSqF.multiply(area).setScale(2, RoundingMode.HALF_UP);
        BigDecimal summedCost = materialCost.add(labourCost).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = summedCost.multiply(taxRate).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = summedCost.add(tax).setScale(2, RoundingMode.HALF_UP);
        return new Order(orderNumber, customerName, stateAbbreviation, taxRate, productName, area, costPerSqF,
                labourCostPerSqF, materialCost, labourCost, tax, total, stateName);
    }

    @Override
    public void addOrder(Order order, LocalDate date) throws FlooringPersistenceException {
        dao.addOrder(order, date);
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws FlooringPersistenceException {
        dao.removeOrder(date, orderNumber);
    }

    @Override
    public void exportData(String exportFileName) throws FlooringPersistenceException {
        dao.exportData(exportFileName);
    }

    @Override
    public String getDefaultFileName() {
        return dao.getDefaultFileName();
    }
}
