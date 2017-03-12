package com.theironyard.charlotte;

import org.h2.engine.Mode;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Sparky {
    Database db = Database.getInstance();
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

    public void start() {
        Spark.init();

        /**************************
         *Spark GET routes
         **************************/

        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap<>();

                    User user = session.attribute("user");
                    if (user == null){
                        return new ModelAndView(m, "index.html");
                    } else {
                        m.put("user", user);
                        return new ModelAndView(m, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/register",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap<>();
                    User user = session.attribute("user");
                    if (user != null){
                        return new ModelAndView(m, "home.html");
                    }else {
                        return new ModelAndView(m, "register.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/home",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    User user = session.attribute("user");
                    if (user == null){
                        return new ModelAndView(m, "register.html");
                    } else {
                        m.put("user", user);
                        return new ModelAndView(m, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    User user = session.attribute("user");
                    if (user == null){
                        return new ModelAndView(m, "register.html");
                    }
                    m.put("user", user);
                    m.put("cart", session.attribute("cart"));
                    return new ModelAndView(m, "cart.html");
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
                    if (session.attribute("user")!= null){
                        response.redirect("home");
                    }
                    else if(db.verifyUser(request.queryParams("userName"), request.queryParams("password"))){
                        session.attribute("user", db.selectUser(request.queryParams("userName")));
                        response.redirect("/home");
                    } else {
                        response.redirect("/");
                    }
                    return "";
                }
        );
        Spark.post(
                "/register",
                (request, response) -> {
                    Session session = request.session();
                    session.attribute("userName", request.queryParams("name"));
                    response.redirect("/");
                    return "";
                }
        );
    }
}
