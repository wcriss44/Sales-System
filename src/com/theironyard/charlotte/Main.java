package com.theironyard.charlotte;

import org.h2.tools.Server;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException{
       // Server.createWebServer().start();
        Database db = Database.getInstance();
        db.createTables();
        Sparky spark = Sparky.getInstance();
        spark.start();

    }









//    public static void testData() throws SQLException{
//        insertItem("Pizza", "fast food", 12, 100.10);
//        insertItem("hamBurger", "order on phone", 1, 3.99);
//        insertItem("pb and J", "has that butter", 3, 42.22);
//        insertItem("shirt", "shoes", 1, .35);
//
//        insertUser("Will", "something", false );
//        insertUser("Mike", "else", true);
//
//        ArrayList<Item> items = new ArrayList<>();
//        items.add(selectItem(1));
//        items.add(selectItem(2));
//        items.add(selectItem(4));
//
//        items.get(0).setOrderAmount(3);
//        items.get(1).setOrderAmount(2);
//        items.get(2).setOrderAmount(44);
//
//        insertOrder(1, items);
//    }
}
//TODO: IMPORTANT! Use ArrayList<Item> to track the "in cart" items. When you do cart.add(new Item(...)), make
//TODO:                                                             sure to use the setOrderAmount() method.
