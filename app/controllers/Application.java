package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.modules.gae.*;

import java.util.*;

import models.*;

import com.google.appengine.api.users.*;

public class Application extends Controller {

    private static final Map<String, String> openIdProviders;
    static {
        openIdProviders = new HashMap<String, String>();
        openIdProviders.put("Google", "google.com/accounts/o8/id");
        openIdProviders.put("Yahoo", "yahoo.com");
        openIdProviders.put("MySpace", "myspace.com");
        openIdProviders.put("AOL", "aol.com");
        openIdProviders.put("MyOpenId.com", "myopenid.com");
    }

    @Before
    static void setUser() {
        User user = GAE.getUser();

        if(user != null) {
            renderArgs.put("user", user);
        }
    }

    public static void index() {
        render();
    }

    public static void sayHello(@Required String myName) {
        if (validation.hasErrors()) {
            flash.error("Oops, please enter your name!");
            index();
        }
        render(myName);
    }

    public static void login(String cont, String openid) {

        // return to this action once logged in
        if (GAE.isLoggedIn()) {
            if (cont != null) {
                redirect(cont);
            } else {
                index();
            }
        }
        
        // if federated login provided, log them in
        if (openid != null) {

            HashMap<String, Object> returnParams = new HashMap<String, Object>();

            if (cont != null) {
                returnParams.put("cont", cont);
            }

            GAE.login("Application.login", returnParams, openid);

        }

        render();
/*
        // default is to give options for logging in
        for (String providerName : openIdProviders.keySet()) {
            String providerUrl = openIdProviders.get(providerName);
            String loginUrl = userService.createLoginURL(req
                    .getRequestURI(), null, providerUrl, attributes);
            out.println("[<a href=\"" + loginUrl + "\">" + providerName + "</a>] ");
        }
*/
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

}
