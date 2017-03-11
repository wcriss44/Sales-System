package com.theironyard.charlotte;

public class User {
    private String name;
    private String initials;
    private String firstName;
    private int id;
    private boolean admin;

    public User(String name, int id, boolean admin) {
        this.name = name;
        this.id = id;
        this.admin = admin;
        String[] names = name.split(" ");
        String firstLetter = names[0].substring(0, 1);
        String secondLetter = names[1].substring(0, 1);
        this.initials = firstLetter + secondLetter;
        this.firstName = names[0];
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
