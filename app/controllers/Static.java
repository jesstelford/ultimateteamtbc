package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.modules.gae.*;

import java.util.*;
import java.io.*;

import models.*;

public class Static extends Controller {

    public static void staticIco(@Required String path) throws FileNotFoundException {
        staticFile(path, "image/x-icon");
    }

    public static void staticTxt(@Required String path) throws FileNotFoundException {
        staticFile(path, "text/plain");
    }

    private static void staticFile(@Required String path, @Required String contentType) throws FileNotFoundException {

        if (validation.hasErrors()) {
            notFound(path);
        }

        File file = play.Play.getFile(path); 
        InputStream is;

        try {
            is = new FileInputStream(file); 
        } catch (FileNotFoundException fnfe) {
            notFound(path);
            return; // to stop the 'is' might not have been initialize warning
        }

        response.setHeader("Content-Length", file.length() + ""); 
        response.cacheFor("2h"); 
        response.contentType = contentType; 
        response.direct = is; //renderBinary() will override any caching headers

    }

}
