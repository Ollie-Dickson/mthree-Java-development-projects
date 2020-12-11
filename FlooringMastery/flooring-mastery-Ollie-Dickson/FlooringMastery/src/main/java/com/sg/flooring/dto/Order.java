package com.sg.flooring.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Order implements Comparable<Order>{
    private int orderNumber;
    private String customerName;
    private String stateAbbreviation;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal labourCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal labourCost;
    private BigDecimal tax;
    private BigDecimal total;
    private String stateName;

    public Order(int orderNumber, String customerName, String stateAbbreviation, BigDecimal taxRate, String productType,
                 BigDecimal area, BigDecimal costPerSquareFoot, BigDecimal labourCostPerSquareFoot,
                 BigDecimal materialCost, BigDecimal labourCost, BigDecimal tax, BigDecimal total, String stateName){
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.stateAbbreviation = stateAbbreviation;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.labourCostPerSquareFoot = labourCostPerSquareFoot;
        this.materialCost = materialCost;
        this.labourCost = labourCost;
        this.tax = tax;
        this.total = total;
        this.stateName = stateName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getProductType() {
        return productType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLabourCostPerSquareFoot() {
        return labourCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public int compareTo(Order other) {
        return Integer.compare(this.getOrderNumber(), other.getOrderNumber());
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", customerName='" + customerName + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", taxRate=" + taxRate +
                ", productType='" + productType + '\'' +
                ", area=" + area +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", labourCostPerSquareFoot=" + labourCostPerSquareFoot +
                ", materialCost=" + materialCost +
                ", labourCost=" + labourCost +
                ", tax=" + tax +
                ", total=" + total +
                ", stateName='" + stateName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber &&
                Objects.equals(customerName, order.customerName) &&
                Objects.equals(stateAbbreviation, order.stateAbbreviation) &&
                Objects.equals(taxRate, order.taxRate) &&
                Objects.equals(productType, order.productType) &&
                Objects.equals(area, order.area) &&
                Objects.equals(costPerSquareFoot, order.costPerSquareFoot) &&
                Objects.equals(labourCostPerSquareFoot, order.labourCostPerSquareFoot) &&
                Objects.equals(materialCost, order.materialCost) &&
                Objects.equals(labourCost, order.labourCost) &&
                Objects.equals(tax, order.tax) &&
                Objects.equals(total, order.total) &&
                Objects.equals(stateName, order.stateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, stateAbbreviation, taxRate, productType, area, costPerSquareFoot,
                labourCostPerSquareFoot, materialCost, labourCost, tax, total, stateName);
    }
}
