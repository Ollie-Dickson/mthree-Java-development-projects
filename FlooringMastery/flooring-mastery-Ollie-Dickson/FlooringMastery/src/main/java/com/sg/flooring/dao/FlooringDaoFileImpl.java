package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.State;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FlooringDaoFileImpl implements FlooringDao{

    private String TAX_FILE;
    private String PRODUCT_FILE;
    private String ORDERS_DIRECTORY;
    private static String DEFAULT_EXPORT_FILE;
    public static final String DELIMITER = ",";
    private Map<String, Product> products = new HashMap<>();
    private Map<String, State> states = new HashMap<>();


    public FlooringDaoFileImpl(){
        this.TAX_FILE = "Data/Taxes.txt";
        this.PRODUCT_FILE = "Data/Products.txt";
        this.DEFAULT_EXPORT_FILE = "Backup/DataExport.txt";
        this.ORDERS_DIRECTORY = "Orders";
    }

    public FlooringDaoFileImpl(String taxFile, String productFile, String exportFile, String ordersDirectory){
        this.TAX_FILE = taxFile;
        this.PRODUCT_FILE = productFile;
        this.DEFAULT_EXPORT_FILE = exportFile;
        this.ORDERS_DIRECTORY = ordersDirectory;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringPersistenceException {
        File ordersDirectory = new File(ORDERS_DIRECTORY);
        File[] ordersFileList = ordersDirectory.listFiles();
        for(File file : ordersFileList){                // American date convention 06012013 - 1st June 2013
            if(file.getName().endsWith(".txt")){
                LocalDate fileDate = LocalDate.parse(file.getName().substring(7, 15), DateTimeFormatter.ofPattern("MMddyyyy"));
                if(fileDate.isEqual(date)){
                    return readOrders(ORDERS_DIRECTORY+ "/"+ file.getName());
                }
            }
        }
        return null; //if none of the files match the given date
    }

    @Override
    public void addOrder(Order order, LocalDate date) throws FlooringPersistenceException {
        List<Order> ordersList = getOrders(date);
        if(ordersList != null){
            ordersList.add(order);
        } else {
            ordersList = new ArrayList<>(Arrays.asList(order));
        }
        writeOrders(ordersList, date);
    }

    @Override
    public void removeOrder(LocalDate date, int orderNumber) throws FlooringPersistenceException {
        List<Order> ordersList = getOrders(date);
        List<Order> updatedList = ordersList.stream()
                .filter((o) -> o.getOrderNumber() != orderNumber)
                .collect(Collectors.toList());
        writeOrders(updatedList, date);
    }

    @Override
    public List<Product> getProducts() throws FlooringPersistenceException {
        loadProducts();
        List<Product> productsList = new ArrayList<>();
        Set<String> keys = products.keySet();
        for(String key : keys){
            productsList.add(products.get(key));
        }
        return productsList;
    }

    @Override
    public List<State> getStates() throws FlooringPersistenceException {
        loadTaxes();
        List<State> statesList = new ArrayList<>();
        Set<String> keys = states.keySet();
        for(String key : keys){
            statesList.add(states.get(key));
        }
        return statesList;
    }

    @Override
    public int getNewOrderNumber() throws FlooringPersistenceException {
        File ordersDirectory = new File(ORDERS_DIRECTORY);
        File[] ordersFileList = ordersDirectory.listFiles();
        int maxOrderNumber = 0;
        for(File file : ordersFileList){
            if(file.getName().endsWith(".txt")){
                List<Order> ordersInFile = readOrders(ORDERS_DIRECTORY +"/"+ file.getName());
                for(Order someOrder : ordersInFile){
                    if(someOrder.getOrderNumber() > maxOrderNumber){
                        maxOrderNumber = someOrder.getOrderNumber();
                    }
                }
            }
        }
        return maxOrderNumber + 1;
    }

    @Override
    public void exportData(String exportFileName) throws FlooringPersistenceException {
        try(PrintWriter out = new PrintWriter(new FileWriter(exportFileName))){
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                    "LabourCostPerSquareFoot,MaterialCost,LabourCost,Tax,Total,Date");
            File ordersDirectory = new File(ORDERS_DIRECTORY);
            File[] ordersFileList = ordersDirectory.listFiles();
            List<Order> allOrders = new ArrayList<>();
            Map<Integer, LocalDate> orderDateMap = new HashMap<>();
            // collect orders from files into a single list and store the dates in a hashmap
            for(File file : ordersFileList){
                if(file.getName().endsWith(".txt")){
                    LocalDate fileDate = LocalDate.parse(file.getName().substring(7, 15), DateTimeFormatter.ofPattern("MMddyyyy"));
                    List<Order> ordersInFile = readOrders(ORDERS_DIRECTORY +"/"+ file.getName());
                    for(Order someOrder : ordersInFile){
                        allOrders.add(someOrder);
                        orderDateMap.put(someOrder.getOrderNumber(), fileDate);
                    }
                }
            }
            // sort the list by order number and write to export file
            List<Order> allOrdersSorted = allOrders.stream()
                    .sorted()
                    .collect(Collectors.toList());
            for(Order someOrder : allOrdersSorted){
                out.println(marshalOrder(someOrder) + DELIMITER +
                        orderDateMap.get(someOrder.getOrderNumber()).format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
                out.flush();
            }
        } catch (IOException e){
            throw new FlooringPersistenceException(
                    "Order data could not be exported.", e);
        }
    }

    @Override
    public String getDefaultFileName() {
        return DEFAULT_EXPORT_FILE;
    }

    private void writeOrders(List<Order> ordersList, LocalDate date) throws FlooringPersistenceException {
        String ORDER_FILE = ORDERS_DIRECTORY +"/Orders_"+ date.format(DateTimeFormatter.ofPattern("MMddyyyy")) +".txt";
        try(PrintWriter out = new PrintWriter(new FileWriter(ORDER_FILE))){
            out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
                    "LabourCostPerSquareFoot,MaterialCost,LabourCost,Tax,Total");
            List<Order> sortedOrders = ordersList.stream()
                    .sorted()
                    .collect(Collectors.toList());
            for(Order someOrder : sortedOrders){
                out.println(marshalOrder(someOrder));
                out.flush();
            }
        } catch (IOException e){
            throw new FlooringPersistenceException(
                    "Could not write orders to file.", e);
        }
        if(ordersList.size() == 0){
            try{
                File deleteThisFile = new File(ORDER_FILE);
                deleteThisFile.delete();
            } catch (Exception e){
                throw new FlooringPersistenceException("--Could not delete file: "+ ORDER_FILE +"--");
            }
        }
    }

    private void loadProducts() throws FlooringPersistenceException {
        Scanner sc;
        try{
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e){
            throw new FlooringPersistenceException(
                    "Could not load product data into memory.", e);
        }
        products.clear();
        String currentLine = sc.nextLine();
        while(sc.hasNextLine()){
            currentLine = sc.nextLine();
            String[] productInfo = currentLine.split(DELIMITER);
            Product product = new Product(productInfo[0], new BigDecimal(productInfo[1]),
                    new BigDecimal(productInfo[2]));
            products.put(productInfo[0], product);
        }
    }

    private void loadTaxes() throws FlooringPersistenceException {
        Scanner sc;
        try{
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e){
            throw new FlooringPersistenceException(
                    "Could not load tax data into memory.", e);
        }
        states.clear();
        String currentLine = sc.nextLine();
        while(sc.hasNextLine()){
            currentLine = sc.nextLine();
            String[] taxInfo = currentLine.split(DELIMITER);
            State state = new State(taxInfo[0], taxInfo[1], new BigDecimal(taxInfo[2]));
            states.put(taxInfo[1], state);
        }
    }

    private List<Order> readOrders(String fileName) throws FlooringPersistenceException{
        Scanner sc;
        try{
            sc = new Scanner(
                    new BufferedReader(
                            new FileReader(fileName)));
        } catch (FileNotFoundException e){
            throw new FlooringPersistenceException(
                    "Could not load data from "+ fileName +".", e);
        }
        List<Order> ordersList = new ArrayList<>();
        String currentLine = sc.nextLine();
        while(sc.hasNextLine()){
            currentLine = sc.nextLine();
            ordersList.add(unmarshalOrder(currentLine));
        }
        return ordersList;
    }

    private Order unmarshalOrder(String orderAsText) throws FlooringPersistenceException{
        String[] orderInfo = orderAsText.split(DELIMITER);
        String stateName = getFullStateName(orderInfo[2]);
        Order order = new Order(Integer.parseInt(orderInfo[0]), orderInfo[1], orderInfo[2],
                new BigDecimal(orderInfo[3]), orderInfo[4], new BigDecimal(orderInfo[5]), new BigDecimal(orderInfo[6]),
                new BigDecimal(orderInfo[7]), new BigDecimal(orderInfo[8]), new BigDecimal(orderInfo[9]),
                        new BigDecimal(orderInfo[10]), new BigDecimal(orderInfo[11]), stateName);
        return order;
    }

    private String getFullStateName(String stateAbbreviation) throws FlooringPersistenceException{
        loadTaxes();
        Set<String> keys = states.keySet();
        for(String key : keys){
            State state = states.get(key);
            if(stateAbbreviation.contains(state.getStateAbbreviation())){
                return state.getStateName();
            }
        }
        return null;
    }

    private String marshalOrder(Order someOrder){
        String orderAsText = someOrder.getOrderNumber() + DELIMITER;
        orderAsText += someOrder.getCustomerName() + DELIMITER;
        orderAsText += someOrder.getStateAbbreviation() + DELIMITER;
        orderAsText += someOrder.getTaxRate() + DELIMITER;
        orderAsText += someOrder.getProductType() + DELIMITER;
        orderAsText += someOrder.getArea() + DELIMITER;
        orderAsText += someOrder.getCostPerSquareFoot() + DELIMITER;
        orderAsText += someOrder.getLabourCostPerSquareFoot() + DELIMITER;
        orderAsText += someOrder.getMaterialCost() + DELIMITER;
        orderAsText += someOrder.getLabourCost() + DELIMITER;
        orderAsText += someOrder.getTax() + DELIMITER;
        orderAsText += someOrder.getTotal();
        return orderAsText;
    }

}
