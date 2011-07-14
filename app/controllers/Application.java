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

    // TO login we redirect them if they are already logged in. We redirect
    // them if they are logging in via OpenId. Lastly, we render the login
    // page.
    public static void login(String continueTo, String openid_identifier) {
        redirectLoggedInUser(continueTo);
        loginViaOpenId(continueTo, openid_identifier);
        render();
    }

    private static void redirectLoggedInUser(String continueTo) {
        if (GAE.isLoggedIn()) {
            redirectToContinuation(continueTo);
        }
    }

    private static void loginViaOpenId(String continueTo, String openId) {
        if (isValidOpenId(openId)) {
            redirectToOpenIdLogin(continueTo, openId);
        }
    }

    private static void redirectToContinuation(String continueTo) {
        if (isValidContinuation(continueTo)) {
            redirect(continueTo);
        }
        index();
    }

    private static boolean isValidOpenId(String openId) {
        return (openId != null);
    }

    private static void redirectToOpenIdLogin(String continueTo, String openId) {

        HashMap<String, Object> returnParams = generateOpenIdReturnParamaters(continueTo);
        openIdLogin(returnParams, openId);
    }

    private static HashMap<String, Object> generateOpenIdReturnParamaters(String continueTo) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (isValidContinuation(continueTo)) {
            parameters.put("continueTo", continueTo);
        }
        return parameters;
    }

    private static void openIdLogin(HashMap<String, Object> paramaters, String openId) {
        try {
            GAE.login("Application.login", paramaters, openId);
        } catch(IllegalArgumentException iae) {
            flash.error(iae.toString());
        }
    }

    private static boolean isValidContinuation(String continueTo) {
        return (continueTo != null);
    }

    public static void logout() {
        GAE.logout("Application.index");
    }
}
