package com.theironyard.charlotte;

import org.h2.tools.Server;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException{
        //Server.createWebServer().start();
        Database db = Database.getInstance();
        db.createTables();
        Sparky spark = Sparky.getInstance();
        spark.start();

        //TODO: Uncomment Server.CreateWebServer().start(); if you need a visual DB
    }
}
