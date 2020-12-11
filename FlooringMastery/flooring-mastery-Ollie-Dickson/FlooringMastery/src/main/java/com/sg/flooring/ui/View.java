package com.sg.flooring.ui;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class View {
    private UserIO io;
    private final String validChars = "0123456789., abcdefghijklmnopqrstuvwxyz";

    @Autowired
    public View(UserIO io){
        this.io = io;
    }

    public int displayMenu(){
        io.print("**********************");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        io.print("********************");
        return io.readInt("Please select from the above choices:", 1, 6);
    }

    public LocalDate getDate() throws InvalidDateException{
        io.print("Please enter a date.");
        int year = io.readInt("Enter the year:", 2000, 2040);
        int month = io.readInt("Enter the month:", 1,12);
        int day = io.readInt("Enter the day:", 1,31);
        String dateString = String.format("%02d", month) + String.format("%02d", day) + year;
        DateTimeFormatter strictFormatter =
                DateTimeFormatter.ofPattern("MMdduuuu").withResolverStyle(ResolverStyle.STRICT);
        try{
            LocalDate date = LocalDate.parse(dateString, strictFormatter);
            return date;
        } catch (DateTimeParseException e){
            throw new InvalidDateException("--INVALID DATE--");
        }
    }

    public void displayOrders(List<Order> ordersList, LocalDate date){
        io.print("** Orders "+ date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) +" **");
        for(Order order : ordersList){
            String orderString = order.getOrderNumber() +". "+ order.getCustomerName().replace('*', ',');
            orderString += ", "+ order.getStateName() +", Product:"+ order.getProductType();
            orderString += ", Area:"+ order.getArea() +"ft², Material Cost:$" + order.getMaterialCost();
            orderString += ", Labour Cost:$"+ order.getLabourCost() +", Tax:$" +order.getTax();
            orderString += ", Total Cost:$"+ order.getTotal();
            io.print(orderString);
        }
        io.readString("** Please hit enter to continue. **");
    }

    public LocalDate getOrderDate() throws InvalidDateException{
        while(true){
            LocalDate date = getDate();
            if(date.isAfter(LocalDate.now())){
                return date;
            } else {
                io.print("Date must be in the future.");
            }
        }
    }

    public String getOrderName() {
        boolean validName;
        while(true){
            String customerName = io.readString("Enter the customer name:");
            validName = true;
            for(int i=0; i<customerName.length(); i++){
                if(!validChars.contains("" + customerName.toLowerCase().charAt(i)) && validName == true){
                    io.print("--INVALID NAME--");
                    io.print("Name must only contain [a-z][0-9] as well as commas and periods.");
                    validName = false;
                }
            }
            if(validName) return customerName.replace(',', '*');;
        }
    }

    public String editOrderName(String currentName){
        boolean validName;
        while(true){
            String newName = io.readString("Enter the customer name ("+ currentName.replace('*', ',') +"):");
            validName = true;
            if(newName.length() == 0) return currentName;
            for(int i=0; i<newName.length(); i++){
                if(!validChars.contains("" + newName.toLowerCase().charAt(i)) && validName == true){
                    io.print("--INVALID NAME--");
                    io.print("Name must only contain [a-z][0-9] as well as commas and periods.");
                    validName = false;
                }
            }
            if(validName) return newName.replace(',', '*');
        }

    }

    public String getOrderState() {
        return io.readString("Enter the name of the state:");
    }

    public String editOrderState(String stateName) {
        String newName = io.readString("Enter the name of the state ("+ stateName +"):");
        if(newName.length() == 0) return stateName;
        return newName;
    }

    public String getOrderProduct(List<Product> productsList) {
        io.print("** Available Products **");
        Map<Integer, String> productIndex = new HashMap<>();
        int n = 1;
        for(Product product : productsList){
            productIndex.put(n, product.getProductType());
            io.print("* "+ n++ +". "+ product.getProductType() +", Cost:$"+ product.getCostPerSquareFoot() +
                    "/ft², Labour Cost:$"+ product.getLabourCostPerSquareFoot() +"/ft²");
        }
        return productIndex.get(io.readInt("Please select from the above choices:", 1, n-1));
    }

    public String editOrderProduct(List<Product> productsList, String currentProduct){
        io.print("** Available Products **");
        Map<Integer, String> productIndex = new HashMap<>();
        int n = 1;
        for(Product product : productsList){
            productIndex.put(n, product.getProductType());
            io.print("* "+ n++ +". "+ product.getProductType() +", Cost:$"+ product.getCostPerSquareFoot() +
                    "/ft², Labour Cost:$"+ product.getLabourCostPerSquareFoot() +"/ft²");
        }
        while(true){
            String input = io.readString("Please select product from the above choices ("+ currentProduct +"):");
            if(input.length() == 0) return currentProduct;
            try{
                int inputInt = Integer.parseInt(input);
                if(inputInt > 1 && inputInt < n){
                    return productIndex.get(inputInt);
                } else {
                    io.print("input does not fall in given range "
                            + "("+ 1 +" - "+ (n-1) +").");
                }
            } catch(NumberFormatException ex) {
                io.print("Input could not be parsed into "
                        + "an integer.");
            }
        }

    }

    public BigDecimal getOrderArea() {
        float input = io.readFloat("Please enter the desired area (minimum order size is 100ft²):",
                100, 10000);
        BigDecimal area = new BigDecimal(Float.toString(input));
        return area.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal editOrderArea(BigDecimal area) {
        while(true){
            String input = io.readString("Please enter the desired area (minimum order size is 100ft² - current" +
                    " area:"+ area +"ft²):");
            if(input.length() == 0) return area;
            BigDecimal newArea = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
            if(newArea.compareTo(new BigDecimal("100")) > 0) return newArea;
            io.print("--Minimum order size is 100ft²--");
        }
    }

    public boolean confirmOrder(Order order) {
        io.print("* Order Confirmation");
        String orderString = order.getOrderNumber() +". "+ order.getCustomerName().replace('*', ',');
        orderString += ", "+ order.getStateName() +", Product:"+ order.getProductType();
        orderString += ", Area:"+ order.getArea() +"ft², Material Cost:$" + order.getMaterialCost();
        orderString += ", Labour Cost:$"+ order.getLabourCost() +", Tax:$" +order.getTax();
        orderString += ", Total Cost:$"+ order.getTotal();
        io.print(orderString);
        while(true){
            String input = io.readString("Would you like to place the order? (Y/N):");
            if(input.toLowerCase().equals("y")){
                return true;
            } else if (input.toLowerCase().equals("n")){
                return false;
            }
        }
    }

    public boolean confirmEdit(Order orderToEdit, Order order) {
        io.print("* Edit Confirmation");
        io.print("- Pre-edit information:");
        String orderString = orderToEdit.getOrderNumber() +". "+ orderToEdit.getCustomerName().replace('*', ',');
        orderString += ", "+ orderToEdit.getStateName() +", Product:"+ orderToEdit.getProductType();
        orderString += ", Area:"+ orderToEdit.getArea() +"ft², Material Cost:$" + orderToEdit.getMaterialCost();
        orderString += ", Labour Cost:$"+ orderToEdit.getLabourCost() +", Tax:$" +orderToEdit.getTax();
        orderString += ", Total Cost:$"+ orderToEdit.getTotal();
        io.print(orderString);
        io.print("- Post-edit information:");
        String editString = order.getOrderNumber() +". "+ order.getCustomerName().replace('*', ',');
        editString += ", "+ order.getStateName() +", Product:"+ order.getProductType();
        editString += ", Area:"+ order.getArea() +"ft², Material Cost:$" + order.getMaterialCost();
        editString += ", Labour Cost:$"+ order.getLabourCost() +", Tax:$" +order.getTax();
        editString += ", Total Cost:$"+ order.getTotal();
        io.print(editString);
        while(true){
            String input = io.readString("Would you like to save changes? (Y/N):");
            if(input.toLowerCase().equals("y")){
                return true;
            } else if (input.toLowerCase().equals("n")){
                return false;
            }
        }
    }

    public Order chooseOrder(List<Order> ordersList, LocalDate date, String prompt, boolean remove){
        io.print("** Orders "+ date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) +" **");
        for(Order order : ordersList){
            String orderString = order.getOrderNumber() +". "+ order.getCustomerName().replace('*', ',');
            orderString += ", "+ order.getStateName() +", Product:"+ order.getProductType();
            orderString += ", Area:"+ order.getArea() +"ft², Material Cost:$" + order.getMaterialCost();
            orderString += ", Labour Cost:$"+ order.getLabourCost() +", Tax:$" +order.getTax();
            orderString += ", Total Cost:$"+ order.getTotal();
            io.print(orderString);
        }
        while(true){
            int selection = io.readInt(prompt);
            for(Order order : ordersList){
                if(order.getOrderNumber() == selection){
                    if(remove){
                        while(true){
                            String input =
                                    io.readString("Are you sure you would like to remove order '"+ order.getOrderNumber() +
                                            ". "+ order.getCustomerName() +"' ? (Y/N):");
                            if(input.toLowerCase().equals("y")){
                                return order;
                            } else if (input.toLowerCase().equals("n")){
                                return null;
                            }
                        }
                    } else {
                        return order;
                    }

                }
            }
            displayErrorMessage("--That number does not match any order--");
        }
    }

    public void displayErrorMessage(String errorMessage){
        String errorBanner = "=== ERROR ===";
        if(errorMessage.length() > 14){
            int spacing = (errorMessage.length() - 13) / 2;
            String space = "";
            for(int i=0; i<spacing; i++){
                space += " ";
            }
            errorBanner = space + errorBanner;
        }
        io.print(errorBanner);
        io.print(errorMessage);
    }

    public void displayRemoveBanner() {
        io.print("** Remove Order **");
    }

    public void displayRemoveSuccess(Order removedOrder) {
        io.print("* Order '"+ removedOrder.getOrderNumber() +". "+ removedOrder.getCustomerName() +"' was removed " +
                "successfully.");
    }

    public void displayDisplayBanner() {
        io.print("** Display Orders **");
    }

    public void displayAddBanner() {
        io.print("** Add an Order **");
    }

    public void displayEditBanner() {
        io.print("** Edit an Order **");
    }

    public void displayAddSuccess(Order order) {
        io.print("* Order '"+ order.getOrderNumber() +". "+ order.getCustomerName().replace('*', ',') +"' was placed " +
                "successfully.");
    }

    public void displayEditSuccess() {
        io.print("* Changes have been saved. *");
    }

    public void displayExportSuccess() {
        io.print("* Data Exported Successfully. *");
    }

    public void displayExitMessage() {
        io.print("* Goodbye.");
    }

    public String getFileName(String defaultFile) {
        io.print("** Export Data **");
        String input = io.readString("Please enter the name of the text file you would like to export to ("+
                defaultFile +"):");
        if(input.length() == 0) return defaultFile;
        if(input.endsWith(".txt")) return input;
        return input + ".txt";
    }

    public void displayActionCancelled() {
        io.print("* Action Cancelled.");
    }
}
