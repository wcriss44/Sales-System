package com.theironyard.charlotte;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    /*******************************
     * Singleton methods and fields
     *******************************/
    private static Database database = new Database();

    private Database(){
        //private constructor ensures that you can't make a new database object
    }

    public static Database getInstance(){
        return database;
        //This is the only way you can get an instance of database
    }

    /*******************************
     * Database connection
     ******************************/

    public static Connection getConnection() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:h2:./main");
        return connection;
    }
    public static void createTables() throws SQLException{
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS users(id IDENTITY, name VARCHAR, password VARCHAR, admin BOOLEAN)");
        statement.execute("CREATE TABLE IF NOT EXISTS items(id IDENTITY, name VARCHAR, description VARCHAR, quantity INTEGER, price DOUBLE)");
        statement.execute("CREATE TABLE IF NOT EXISTS orders(id INT, item_id INTEGER, user_id INTEGER, quantity INTEGER)");
    }

    /*******************************
     * User SQL methods
     *******************************/
    public User selectUser(String name) throws SQLException {
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
    public void insertUser(String name, String password, boolean admin) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES(NULL, ?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, password);
        statement.setBoolean(3, admin);
        statement.execute();
    }
    public boolean verifyUser(String userName, String password) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE name = ?");
        statement.setString(1, userName);
        ResultSet results = statement.executeQuery();
        String currentPass = null;
        while(results.next()){
            currentPass = results.getString("password");
        }
        return password.equals(currentPass);
    }

    /*******************************
     * Item SQL methods
     *******************************/

    public void insertItem(String name, String description, int quantity, double price) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO items VALUES(NULL, ?, ?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setInt(3, quantity);
        statement.setDouble(4, price);
        statement.execute();
    }
    public void updateItem(int id, String name, String description, int quantity, double price) throws SQLException{
        //this method is for ADMIN only!
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE items SET name = ?, description = ?, quantity = ?, price = ?  WHERE  id = ?");
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setInt(3, quantity);
        statement.setDouble(4, price);
        statement.setInt(5, id);
        statement.execute();
    }
    public void removeItem(int id) throws SQLException{
        //this method is for ADMIN only!
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ? ");
        statement.setInt(1, id);
        statement.execute();
    }
    public Item selectItem(int id) throws SQLException{
        //method to return a single item based on index ID
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE id = ? ");
        statement.setInt(1, id);

        String name = null;
        String description = null;
        int quantity = 0;
        double price = 0;
        ResultSet results = statement.executeQuery();
        while(results.next()){
            name = results.getString("name");
            description = results.getString("description");
            quantity = results.getInt("quantity");
            price = results.getDouble("price");
        }
        return new Item(name,description, quantity, price, id);
    }
    public ArrayList<Item> selectItems() throws SQLException{
        Connection connection = getConnection();
        ArrayList<Item> items = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM items");

        ResultSet results = statement.executeQuery();
        while(results.next()){
            String name = results.getString("name");
            String description = results.getString("description");
            int quantity = results.getInt("quantity");
            double price = results.getDouble("price");
            int id = results.getInt("id");
            items.add(new Item(name, description, quantity, price, id));
        }
        return items;
    }

    /*******************************
     * Order SQL methods
     *******************************/

    //this method uses two joins for a total of three tables(users, items, orders). The query results are then used to create an
   // Order object which has an ArrayList of items. This ArrayList contains all items in the order.
    public Order selectOrder(int orderId, int userId) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT users.name, orders.id, items.id, items.name, items.price, items.description, orders.quantity FROM orders INNER JOIN items ON orders.item_id = items.id JOIN users ON orders.user_id = ? WHERE (orders.id = ? AND users.id = ?)");
        statement.setInt(1, userId);
        statement.setInt(2, orderId);
        statement.setInt(3, userId);

        Order order = null;
        ArrayList<Item> items = new ArrayList<>();
        String buyer = null;

        ResultSet result = statement.executeQuery();
        while(result.next()){
            buyer = result.getString("users.name");
            String name = result.getString("items.name");
            String description = result.getString("items.description");
            Double price = result.getDouble("items.price");
            int id =result.getInt("items.id");
            int orderAmount = result.getInt("orders.quantity");
            items.add(new Item(name, description, price, id, orderAmount));
        }
        order = new Order(orderId, items, buyer);
        return order;
    }
    public void insertOrder(int userId, ArrayList<Item> items) throws SQLException{
        Connection connection = getConnection();
        PreparedStatement getId = connection.prepareStatement("SELECT id FROM orders");
        ResultSet results = getId.executeQuery();

        int id = 0;
        while(results.next()){
            if (results.getInt("id") >= id){
                id++;
            }
        }
        for(Item item: items){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO orders VALUES(?, ?, ?, ?)");
            statement.setInt(1, id);
            statement.setInt(2, item.getId());
            statement.setInt(3, userId);
            statement.setInt(4, item.getOrderAmount());
            statement.execute();
        }
    }
}
