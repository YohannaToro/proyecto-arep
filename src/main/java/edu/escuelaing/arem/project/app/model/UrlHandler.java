package edu.escuelaing.arem.project.app.model;

import java.lang.reflect.Method;

public class UrlHandler implements Handlers{
    private Method method;

    public UrlHandler(Method m){
        this.method=m;

    }

    public String process() {
        try{
            return (String) method.invoke(null, null);
        }catch(Exception e){
            e.printStackTrace();
            return e.toString();
        }
   
    }

}