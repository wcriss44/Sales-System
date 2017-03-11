package com.theironyard.charlotte;

public class Item {

    private String name;
    private String description;
    private int quantity;
    private int price;
    private int id;

    /*******************************
     * Constructor
     *******************************/

    public Item(String name, String description, int quantity, int price, int id) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
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

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }
}
