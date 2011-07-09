package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import models.*;

import com.google.appengine.api.users.*;

public class Application extends Controller {

    public static void index() {

/*
        String thisURL = request.getRequestURI();
        response.setContentType("text/html");
        if (request.getUserPrincipal() != null) {
            response.getWriter().println("<p>Hello, " +
                                         request.getUserPrincipal().getName() +
                                         "!  You can <a href=\"" +
                                         userService.createLogoutURL(thisURL) +
                                         "\">sign out</a>.</p>");
        } else {
            response.getWriter().println("<p>Please <a href=\"" +
                                         userService.createLoginURL(thisURL) +
                                         "\">sign in</a>.</p>");
        }
*/
        render();
    }

    public static void sayHello(@Required String myName) {
        if (validation.hasErrors()) {
            flash.error("Oops, please enter your name!");
            index();
        }
        render(myName);
    }

    public static void openId(String continue, String openid) {
 
        if (openid.length() == 0) {
            render(continue);
        } else {
            redirect(
                userService.createLoginURL(
                    continue,
                    null,
                    openid,
                    new HashSet()
                )
            );
        }

    }

}
