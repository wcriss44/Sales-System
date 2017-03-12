package com.theironyard.charlotte;

import org.h2.engine.Mode;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.SQLException;
import java.util.ArrayList;
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
                "/cart",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    User user = session.attribute("user");
                    if (user == null){
                        return new ModelAndView(m, "register.html");
                    }
                    m.put("user", user);
                    m.put("cart", session.attribute("cart"));
                    m.put("subTotal", session.attribute("subTotal"));
                    m.put("tax", session.attribute("tax"));
                    m.put("total", session.attribute("total"));
                    return new ModelAndView(m, "cart.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/browse",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    User user = session.attribute("user");
                    if (user == null){
                        return new ModelAndView(m, "register.html");
                    }
                    m.put("user", user);
                    m.put("inventory", db.selectItems());
                    return new ModelAndView(m, "browse.html");
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
                    //needs building
                    Session session = request.session();
                    session.attribute("userName", request.queryParams("name"));
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/add-cart",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") == null){
                        response.redirect("/register");
                        return "";
                        //Redirect if not logged in
                    }
                         //Check for no items
                    if (request.queryParams("quantity").equals("") || Integer.valueOf(request.queryParams("quantity")) <= 0){
                        response.redirect("/browse");
                        return "";
                    }
                    int quantity = Integer.valueOf(request.queryParams("quantity"));
                    int id = Integer.valueOf(request.queryParams("id"));

                    addItem(quantity, id, session);
                    response.redirect("/browse");
                    return "";
                }
        );
        Spark.post(
                "/remove-cart",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") == null){
                        response.redirect("/register");
                        return "";
                        //Redirect if not logged in
                    }
                    int id = Integer.valueOf(request.queryParams("id"));
                        removeCartItem(session, id);
                        populateTotals(session);
                        response.redirect("/cart");
                        return"";
                }
        );
    }

    /**********************************
     * Methods for doing cart logic
     *********************************/
    public void addItem(int quantity, int id, Session session) throws SQLException{
        if (session.attribute("cart") == null){
            session.attribute("cart", new ArrayList<Item>());
            //Create initial cart if needed
        }
        ArrayList<Item> items = session.attribute("cart");
        for (Item item: items){
            if (item.getId() == id) {
                //Add quantity if item exist in cart
                item.setOrderAmount(item.getOrderAmount() + quantity);
                return;
            }
        }
        Item item = db.selectItem(id);

        item.setOrderAmount(Integer.valueOf(quantity));
        items.add(item);
        populateTotals(session);
    }
    public void populateTotals(Session session){
        //Variables needed so I can add sub and tax
        ArrayList<Item> items = session.attribute("cart");
        double sub = subTotal(items);
        double tax = getTax(items);
        double total = sub + tax;
        session.attribute("subTotal", sub);
        session.attribute("tax", tax);
        session.attribute("total", total);
    }
    public double getTax(ArrayList<Item> cart){
        return .07 * subTotal(cart);
    }
    public double subTotal(ArrayList<Item> cart){
        double total = 0;
        for(Item item: cart){
            total += item.getPrice() * item.getOrderAmount();
        }
        return total;
    }
    public void removeCartItem(Session session, int id){
        ArrayList<Item> items = session.attribute("cart");
        Item removal = null;
        for(Item item: items){
            if (item.getId() == id){
                removal = item;
                break;
            }
        }
        items.remove(removal);
    }
}
