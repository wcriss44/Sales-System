package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    /*******************************
     * Database connection
     ******************************/

    public static Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:h2:./main");
        return connection;
    }

    public static void main(String[] args) {


        /**************************
         *Spark GET routes
         **************************/



        /**************************
         *Spark POST routes
         **************************/

    }

    /*******************************
     * User SQL methods
     *******************************/
    public static User selectUser(String name) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
        statement.setString(1, name);
        ResultSet result = statement.executeQuery();
        int id = 0;
        boolean admin = false;
        while(result.next()){
            id = result.getInt("id");
            name = result.getString("name");
            admin = result.getBoolean("admin");
        }
        return new User(name, id, admin);
    }
    public static void insertUser(String name, String password, boolean admin) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, password);
        statement.setBoolean(3, admin);
        statement.execute();
    }


    /*******************************
     * Item SQL methods
     *******************************/

    public static void insertItem(String name, String description, int quantity, int price) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO items VALUES(NULL, ?, ?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setInt(3, quantity);
        statement.setInt(4, price);
        statement.execute();
    }
    public static void updateItem(int id, String name, String description, int quantity, int price) throws SQLException{
        //this method is for ADMIN only!
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE items SET name = ?, description = ?, quantity = ?, price = ?  WHERE  id = ?");
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setInt(3, quantity);
        statement.setInt(4, price);
        statement.setInt(5, id);
        statement.execute();
    }
    public static void removeItem(int id) throws SQLException{
        //this method is for ADMIN only!
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ? ");
        statement.setInt(1, id);
        statement.execute();
    }
    public static Item selectItem(int id) throws SQLException{
        //method to return a single item based on index ID
        Connection connection = Main.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE id = ? ");
        statement.setInt(1, id);

        String name = null;
        String description = null;
        int quantity = 0;
        int price = 0;
        ResultSet results = statement.executeQuery();
        while(results.next()){
            name = results.getString("name");
            description = results.getString("description");
            quantity = results.getInt("quantity");
            price = results.getInt("price");
        }
        return new Item(name,description, quantity, price, id);
    }
    public static ArrayList<Item> selectItems() throws SQLException{
        Connection connection = Main.getConnection();
        ArrayList<Item> items = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM items");

        ResultSet results = statement.executeQuery();
        while(results.next()){
            String name = results.getString("name");
            String description = results.getString("description");
            int quantity = results.getInt("quantity");
            int price = results.getInt("price");
            int id = results.getInt("id");
            items.add(new Item(name, description, quantity, price, id));
        }
        return items;
    }

    /*******************************
     * Order SQL methods
     *******************************/

    public static Order selectOrder(String name, int id) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
        statement.setString(1, name);
        ResultSet result = statement.executeQuery();
        int id = 0;
        boolean admin = false;
        while(result.next()){
            id = result.getInt("id");
            name = result.getString("name");
            admin = result.getBoolean("admin");
        }
        return new User(name, id, admin);
    }
    public static void insertOrder(String name, String password, boolean admin) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, password);
        statement.setBoolean(3, admin);
        statement.execute();
    }
}
