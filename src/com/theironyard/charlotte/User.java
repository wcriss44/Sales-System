package com.theironyard.charlotte;

import java.util.ArrayList;

public class User {
    private String name;
    private String initials;
    private int id;
    private boolean admin;
    private String city;
    private String state;
    private int zip;
    private ArrayList<Order> orders = new ArrayList<>();

    public User(String name, int id, boolean admin, ArrayList<Order> orders, String city, String state, int zip) {
        this.name = name;
        this.id = id;
        this.admin = admin;
        String firstLetter = name.substring(0, 1);
        String secondLetter = name.substring(name.length() - 1).toUpperCase();
        this.initials = firstLetter + secondLetter;
        this.orders = orders;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    /*******************************
     * Getters
     *******************************/
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public boolean isAdmin() {
        return admin;
    }
    public String getInitials(){
        return initials;
    }
    public ArrayList<Order> getOrders(){
        return orders;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZip() {
        return zip;
    }

    /*******************************
     * Setters
     *******************************/

    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public void setInitials(String initials){
        this.initials = initials;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
