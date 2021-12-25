package com.example.shopsmart.Entity;

public class CapacityProduct {
    private int idCapacity;

    private String capacity;
    private int price;

    public CapacityProduct(int idCapacity, String capacity, int price) {
        this.idCapacity = idCapacity;
        this.capacity = capacity;
        this.price = price;
    }

    public int getIdCapacity() {
        return idCapacity;
    }

    public void setIdCapacity(int idCapacity) {
        this.idCapacity = idCapacity;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
