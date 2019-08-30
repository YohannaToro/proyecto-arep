package edu.escuelaing.arem.project.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Handler;

import edu.escuelaing.arem.project.app.model.Handlers;
import edu.escuelaing.arem.project.app.model.UrlHandler;

public class AppServer {

    private static HashMap<String,Handlers> hm= new HashMap<String,Handlers>();


    public static void escuchar() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35001);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();

    }

    public static void inicializar() {
        bind("edu.escuelaing.arem.project.app.Browser");
        System.out.println(hm.get("Browser"));
    }

    public static void bind(String classpath){
        try {
            Class c = Class.forName(classpath);
            for(Method m: c.getMethods()){
                if(m.isAnnotationPresent(Web.class)){
                    Handlers h= new UrlHandler(m);
                    System.out.println("hhhh");
                    hm.put(m.getAnnotation(Web.class).value(), new UrlHandler(m));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}