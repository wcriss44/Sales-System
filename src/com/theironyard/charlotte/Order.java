package com.theironyard.charlotte;

import java.util.ArrayList;

public class Order {
    private int id;
    private String buyer;
    private ArrayList<Item> items = new ArrayList<>();

    /*******************************
     * Constructor
     *******************************/

    public Order(int id, ArrayList<Item> items, String buyer) {
        this.id = id;
        this.items = items;
    }

    /*******************************
     * Getters
     *******************************/

    public int getId(){
        return id;
    }
    public ArrayList<Item> getItems(){
        return items;
    }
    public String getBuyer(){
        return buyer;
    }

    /*******************************
     * Setters
     *******************************/

    public void setId(int id){
        this.id = id;
    }
    public void setBuyer(String buyer){
        this.buyer = buyer;
    }
}
