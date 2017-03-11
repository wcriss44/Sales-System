package com.theironyard.charlotte;

public class User {
    private String name;
    private int id;
    private boolean admin;

    public User(String name, int id, boolean admin) {
        this.name = name;
        this.id = id;
        this.admin = admin;
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
}
