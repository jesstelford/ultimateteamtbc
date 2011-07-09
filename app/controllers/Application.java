package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.modules.gae.*;

import java.util.*;

import models.*;

import com.google.appengine.api.users.*;

public class Application extends Controller {

    public static void index() {

        String currentUser = "";

        if(GAE.isLoggedIn()) {
            currentUser = GAE.getUser().toString();
        }

        render(currentUser);
    }

    public static void sayHello(@Required String myName) {
        if (validation.hasErrors()) {
            flash.error("Oops, please enter your name!");
            index();
        }
        render(myName);
    }

    public static void login(String openid) {
        GAE.login("Application.index", openid);
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

}
