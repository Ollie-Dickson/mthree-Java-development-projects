package com.sg.vendingmachine.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private String name;
    private BigDecimal cost;
    private int inventory;

    public Item(String name, BigDecimal cost, int inventory){
        this.name = name;
        this.cost = cost;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return inventory == item.inventory &&
                Objects.equals(name, item.name) &&
                Objects.equals(cost, item.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, inventory);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", inventory=" + inventory +
                '}';
    }
}
