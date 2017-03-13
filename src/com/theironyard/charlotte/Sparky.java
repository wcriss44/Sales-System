package com.theironyard.charlotte;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Sparky {
    private DecimalFormat df2 = new DecimalFormat(".##");
    private Database db = Database.getInstance();
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
                        m.put("inventory", db.selectItems());
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
        Spark.get(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    session.invalidate();
                    m.put("inventory", db.selectItems());
                    return new ModelAndView(m, "index.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/check-out",
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
                    return new ModelAndView(m, "checkout.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/manage-items",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    User user = session.attribute("user");
                    if (user == null){
                        return new ModelAndView(m, "register.html");
                    }
                    if (user.getAdmin()){
                        m.put("user", user);
                        m.put("inventory", db.selectItems());
                        return new ModelAndView(m, "manageitems.html");
                    } else {
                        return new ModelAndView(m, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );

        /**************************
         *Spark POST routes
         **************************/
        Spark.post(
                "/remove-item",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") == null){
                        response.redirect("/register");
                    }
                    User user = session.attribute("user");
                    if (user.getAdmin()){
                        db.removeItem(Integer.valueOf(request.queryParams("id")));
                        response.redirect("/manage-items");
                        return "";
                    } else {
                        response.redirect("/home");
                        return "";
                    }
                }
        );
        Spark.post(
                "/edit-item",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") == null){
                        response.redirect("/register");
                    }
                    User user = session.attribute("user");
                    if (user.getAdmin()){
                        Item item = db.selectItem(Integer.valueOf(request.queryParams("id")));
                        if (Integer.valueOf(request.queryParams("quantity")) < 0){
                            response.redirect("/manage-items");
                            return "";
                        }
                        db.updateItem(item.getId(), item.getName(), item.getDescription(), Integer.valueOf(request.queryParams("quantity")), item.getPrice());
                        response.redirect("/manage-items");
                        return "";
                    } else {
                        response.redirect("/home");
                        return "";
                    }
                }
        );
        Spark.post(
                "/add-item",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") == null){
                        response.redirect("/register");
                    }
                    User user = session.attribute("user");
                    if (user.getAdmin()){
                        String name = request.queryParams("name");
                        String description = request.queryParams("description");
                        String quantity = request.queryParams("quantity");
                        String price = request.queryParams("price");

                        //Check for bad entries
                        if (name.equals("") || description.equals("") || quantity.equals("") || price.equals("")){
                            response.redirect("/manage-items");
                            return "";
                        } else if (name == null || description == null || quantity == null || price == null){
                            response.redirect("/manage-items");
                            return "";
                        }
                        try {
                            db.insertItem(name, description, Integer.valueOf(quantity), Double.valueOf(price));
                            response.redirect("/manage-items");
                            return "";
                        } catch (Exception e){
                            response.redirect("/manage-items");
                            return "";
                        }
                    } else {
                        response.redirect("/home");
                        return "";
                    }
                }
        );
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
                        response.redirect("/register");
                    }
                    return "";
                }
        );
        Spark.post(
                "/register",
                (request, response) -> {
                    //needs building
                    Session session = request.session();
                    String userName = request.queryParams("userName");
                    String password = request.queryParams("password");
                    if (userVerification(userName, password, request.queryParams("zip"))) {
                        String city = request.queryParams("city");
                        String state = request.queryParams("state");
                        int zip = Integer.valueOf(request.queryParams("zip"));
                        double taxRate = (Double.valueOf(getApiTax(zip) ) / 100);
                        boolean adminStatus = false;


                        if (request.queryParams("adminStatus").equals("yes")){
                            adminStatus = true;
                        }
                        db.insertUser(userName, password, adminStatus, city, state, zip,taxRate);
                        session.attribute("user", db.selectUser(userName));
                        response.redirect("/home");
                        return "";

                    } else {
                        response.redirect("/register");
                        return "";
                    }
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
        Spark.post(
                "/remove-cart-checkout",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") != null){
                        int id = Integer.valueOf(request.queryParams("id"));
                        removeCartItem(session, id);
                        populateTotals(session);
                        response.redirect("/check-out");
                        return"";
                    } else {
                        response.redirect("/register");
                        return "";
                        //Redirect if not logged in
                    }
                }
        );
        Spark.post(
                "/checkout",
                (request, response) -> {
                    Session session = request.session();
                    if (session.attribute("user") == null){
                        response.redirect("/register");
                        return "";
                    }
                    User user = session.attribute("user");
                    String userName = user.getName();
                    ArrayList<Item> cart = session.attribute("cart");
                    db.insertOrder(user.getId(), cart, session.attribute("subTotal"),session.attribute("tax"), session.attribute("total"));
                    for (Item item: cart){
                        db.updateItem(item.getId(), item.getName(), item.getDescription(), (item.getQuantity() - item.getOrderAmount()), item.getPrice());
                    }
                    session.removeAttribute("cart");
                    session.removeAttribute("subTotal");
                    session.removeAttribute("tax");
                    session.removeAttribute("total");

                    session.removeAttribute("user");
                    session.attribute("user", db.selectUser(userName));

                    response.redirect("/home");
                    return "";
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
                if (item.getOrderAmount() + quantity > item.getQuantity()){
                    return;
                }
                item.setOrderAmount(item.getOrderAmount() + quantity);
                populateTotals(session);
                return;
            }
        }
        Item item = db.selectItem(id);
        if (quantity > db.selectItem(id).getQuantity()){
            return;
        }

        item.setOrderAmount(quantity);
        items.add(item);
        populateTotals(session);
    }
    public void populateTotals(Session session){
        //Variables needed so I can add sub and tax
        ArrayList<Item> items = session.attribute("cart");
        User user = session.attribute("user");
        double sub = Double.valueOf(df2.format((subTotal(items))));
        double tax = Double.valueOf(df2.format(getTax(items, user.getTaxRate())));
        double total = Double.valueOf(df2.format(sub + tax));
        session.attribute("subTotal", sub);
        session.attribute("tax", tax);
        session.attribute("total", total);
    }
    public double getTax(ArrayList<Item> cart, double taxRate){

        return taxRate * subTotal(cart);
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

    /******************************
     * Methods for user
     *****************************/
    public boolean userVerification(String name, String password, String zip) throws SQLException{
        ArrayList<String> userNames = db.selectUserNames();
        for (String userName: userNames){
            if (name.equals(userName)){
                return false;
            }
        }
        if (password.length() < 8){
            return false;
        }
        try {
            if (getApiTax(Integer.valueOf(zip)) == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e){
            return false;
        }
    }
    public String getApiTax(int zip) {
        String url = "https://taxrates.api.avalara.com:443/postal?country=usa&postal=";
        String taxRate = null;
        url += zip;
        url += "&apikey=805mYBrUSwE8K6waoX8W2QKHfr9ynITxbVtYsg1WzgD9brvKMj%2BSy8rDN614skUPnAiou5pXWAJm%2BstD7RrUrA%3D%3D";
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("totalRate")) {
                    taxRate = inputLine.substring(15, 18);

                }
            }
            in.close();
            return taxRate;
        } catch (Exception e){
            return null;
        }
    }
}
