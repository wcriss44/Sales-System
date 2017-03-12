package com.theironyard.charlotte;

import java.util.ArrayList;

public class User {
    private String name;
    private String initials;
    private int id;
    private boolean admin;
    private ArrayList<Order> orders = new ArrayList<>();

    public User(String name, int id, boolean admin, ArrayList<Order> orders) {
        this.name = name;
        this.id = id;
        this.admin = admin;
        String firstLetter = name.substring(0, 1);
        String secondLetter = name.substring(name.length() - 1).toUpperCase();
        this.initials = firstLetter + secondLetter;
        this.orders = orders;
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
}
