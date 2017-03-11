package com.theironyard.charlotte;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Sparky {
    /*******************************
     * Singleton methods and fields
     *******************************/
    private static Sparky sparky = new Sparky();

    private Sparky(){
        //private constructor ensures that you can't make a new database object
    }

    public static Sparky getInstance(){
        return sparky;
        //This is the only way you can get an instance of database
    }
    /**************************
     * Start the Spark
     **************************/

    public static void start() {
        Spark.init();

        /**************************
         *Spark GET routes
         **************************/

        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap<>();
                    m.put("Hello", session.attribute("userName"));
                    return new ModelAndView(m, "index.html");
                },
                new MustacheTemplateEngine()
        );

        /**************************
         *Spark POST routes
         **************************/
        Spark.post(
                "/login",
                (request, response) -> {
                    Session session = request.session();
                    session.attribute("userName", request.queryParams("name"));
                    response.redirect("/");
                    return "";
                }
        );
    }
}
