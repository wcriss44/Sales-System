package com.theironyard.charlotte;

public class Item {

    private String name;
    private String description;
    private int quantity;
    private int id;
    private int orderAmount;
    private double price;

    /*******************************
     * Constructor
     *******************************/

    public Item(String name, String description, int quantity, double price, int id) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
    }
    public Item(String name, String description,  double price, int id, int orderAmount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.id = id;
        this.orderAmount = orderAmount;
    }

    /*******************************
     * Getters
     *******************************/

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    /*******************************
     * Setters
     *******************************/

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }
}
