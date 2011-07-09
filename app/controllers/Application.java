package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.modules.gae.*;

import java.util.*;

import models.*;

import com.google.appengine.api.users.*;

public class Application extends Controller {

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

    public static void login(String cont, String openid_identifier) {

        // return to this action once logged in
        if (GAE.isLoggedIn()) {
            if (cont != null) {
                redirect(cont);
            } else {
                index();
            }
        }
        
        // if federated login provided, log them in
        if (openid_identifier != null) {

            HashMap<String, Object> returnParams = new HashMap<String, Object>();

            if (cont != null) {
                returnParams.put("cont", cont);
            }

            try {
                GAE.login("Application.login", returnParams, openid_identifier);
            } catch(IllegalArgumentException iae) {
                flash.error(iae.toString());
            }

        }

        render();
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

}
