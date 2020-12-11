package com.sg.flooring.controller;

import com.sg.flooring.dao.FlooringPersistenceException;
import com.sg.flooring.dto.Order;
import com.sg.flooring.service.FlooringServiceLayer;
import com.sg.flooring.service.NoOrdersFoundException;
import com.sg.flooring.service.StateNotFoundException;
import com.sg.flooring.ui.InvalidDateException;
import com.sg.flooring.ui.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class Controller {
    private View view;
    private FlooringServiceLayer service;

    @Autowired
    public Controller(View view, FlooringServiceLayer service){
        this.view = view;
        this.service = service;
    }

    public void run(){
        boolean notFinished = true;
        int selectedOption;
        try{
            while(notFinished){
                selectedOption = getMenuSelection();
                switch(selectedOption){
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        notFinished = false; //Quit Program
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();
        } catch(FlooringPersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection(){
        return view.displayMenu();
    }

    private void displayOrders() throws FlooringPersistenceException {
        try{
            view.displayDisplayBanner();
            LocalDate date = view.getDate();
            List<Order> ordersLists = service.getOrders(date);
            if(ordersLists == null){throw new NoOrdersFoundException("--NO ORDERS ASSOCIATED WITH THAT DATE--");}
            view.displayOrders(ordersLists, date);
        } catch (InvalidDateException|NoOrdersFoundException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void addOrder() throws FlooringPersistenceException {
        boolean isValid = false;
        view.displayAddBanner();
        LocalDate date = LocalDate.now();
        do{
            try{
                date = view.getOrderDate();
                isValid = true;
            } catch (InvalidDateException e){
                view.displayErrorMessage(e.getMessage());
            }
        } while(!isValid);
        String customerName = view.getOrderName();
        String stateName = "";
        do{
            try{
                isValid = false;
                stateName = view.getOrderState();
                if(!service.checkStateName(stateName)){
                    throw new StateNotFoundException("--INVALID STATE--");
                }
                isValid = true;
            } catch (StateNotFoundException e){
                view.displayErrorMessage(e.getMessage());
            }
        } while(!isValid);
        String productName = view.getOrderProduct(service.getProducts());
        BigDecimal area = view.getOrderArea();
        Order order = service.createOrder(customerName, stateName, productName, area, 0);
        if(view.confirmOrder(order)){
            service.addOrder(order, date);
            view.displayAddSuccess(order);
        }
    }

    private void editOrder() throws FlooringPersistenceException {
        try{
            view.displayEditBanner();
            LocalDate date = view.getDate();
            List<Order> ordersList = service.getOrders(date);
            if(ordersList == null){throw new NoOrdersFoundException("--NO ORDERS ASSOCIATED WITH THAT DATE--");}
            Order orderToEdit = view.chooseOrder(ordersList, date, "Please select the number of the order " +
                    "you would like to edit:", false);
            String customerName = view.editOrderName(orderToEdit.getCustomerName());
            String stateName = "";
            boolean isValid = false;
            do{
                try{
                    stateName = view.editOrderState(orderToEdit.getStateName());
                    if(!service.checkStateName(stateName)){
                        throw new StateNotFoundException("--INVALID STATE--");
                    }
                    isValid = true;
                } catch (StateNotFoundException e){
                    view.displayErrorMessage(e.getMessage());
                }
            } while (!isValid);
            String productName = view.editOrderProduct(service.getProducts(), orderToEdit.getProductType());
            BigDecimal area = view.editOrderArea(orderToEdit.getArea());
            Order order = service.createOrder(customerName, stateName, productName, area, orderToEdit.getOrderNumber());
            if(view.confirmEdit(orderToEdit, order)){
                service.removeOrder(date, orderToEdit.getOrderNumber());
                service.addOrder(order, date);
                view.displayEditSuccess();
            }
        } catch (InvalidDateException|NoOrdersFoundException e){
            view.displayErrorMessage(e.getMessage());
        }

    }

    private void removeOrder() throws FlooringPersistenceException {
        try{
            view.displayRemoveBanner();
            LocalDate date = view.getDate();
            List<Order> ordersList = service.getOrders(date);
            if(ordersList == null){throw new NoOrdersFoundException("--NO ORDERS ASSOCIATED WITH THAT DATE--");}
            Order orderToRemove = view.chooseOrder(ordersList, date, "Please select the number of the order you " +
                    "would like to remove:", true);
            if(orderToRemove != null){
                service.removeOrder(date, orderToRemove.getOrderNumber());
                view.displayRemoveSuccess(orderToRemove);
            } else {
                view.displayActionCancelled();
            }
        } catch (InvalidDateException|NoOrdersFoundException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void exportData() throws FlooringPersistenceException {
        String defaultExportFile = service.getDefaultFileName();
        String exportFileName = view.getFileName(defaultExportFile);
        service.exportData(exportFileName);
        view.displayExportSuccess();
    }

    private void unknownCommand() {
        view.displayErrorMessage("--UNKNOWN COMMAND--");
    }

    private void exitMessage(){
        view.displayExitMessage();
    }
}
