package com.theironyard.charlotte;

import java.util.ArrayList;

public class Order {
    private int id;
    private String buyer;
    private double subTotal, tax, total;
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

    public double getSubTotal() {
        return subTotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
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

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
