package edu.escuelaing.arem.project.app;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.HashMap;
import java.util.logging.Handler;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;


import javax.print.DocFlavor.STRING;

import edu.escuelaing.arem.project.app.model.Handlers;
import edu.escuelaing.arem.project.app.model.UrlHandler;

public class AppServer {

    private static HashMap<String, Handlers> hm = new HashMap<String, Handlers>();

    public static void escuchar() throws IOException {
      
 
        while (true) {
            ServerSocket serverSocket = null;
	  try {
	   serverSocket = new ServerSocket(getPort());
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
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                int index = inputLine.indexOf("/apps/");
                String resource = "", urlInputLine ="";
                int i = -1;

                String headr="HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n \r\n";

                if (index != -1){
                    for (i = index; i < inputLine.length() && inputLine.charAt(i) != ' '; i++) {
                        resource += inputLine.charAt(i);
                    }
                }else{
                    i = inputLine.indexOf('/') + 1;
                }
                if (inputLine.contains("/apps/")) {
                    try {
                        out.println(headr);
                        System.err.println("What " + resource);
                        out.println(hm.get(resource).process());
                    } catch (Exception e) {
                    }
                } else if (inputLine.contains("html")) {
                    //   int i = inputLine.indexOf('/') + 1;

                    while (!urlInputLine.endsWith(".html") && i < inputLine.length()) {
                        urlInputLine += (inputLine.charAt(i++));
                    }
                    System.err.println("Hola amigos " + urlInputLine);
                    String urlDirectoryServer = System.getProperty("user.dir") + "/recursos/" + urlInputLine;
                    System.out.println(urlDirectoryServer);
                    try {

                        BufferedReader readerFile = new BufferedReader(
                                new InputStreamReader(new FileInputStream(urlDirectoryServer), "UTF8"));
                        out.println(headr);
                        while (readerFile.ready()) {
                            out.println(readerFile.readLine());
                        }
                    } catch (FileNotFoundException e) {
                        // out.println("HTTP/2.0 404 Not found.");
                        // out.println("Content-Type: text/html");
                        // out.println("\r\n");
                    }
                } else if (inputLine.contains("jpg")) {
                    while (!urlInputLine.endsWith(".jpg") && i < inputLine.length()) {
                        urlInputLine += (inputLine.charAt(i++));
                    }
                    BufferedImage github = ImageIO
                            .read(new File(System.getProperty("user.dir") + "/recursos/" + urlInputLine));
                    out.println(headr);
                   
                    ImageIO.write(github, "jpg", clientSocket.getOutputStream());
                }
                if (!in.ready()) {
                    break;
                }

            }
            out.close();
            //in.close();
            clientSocket.close();
            serverSocket.close();
        }

    }

    public static void inicializar() {
        try {

            String p = "edu.escuelaing.arem.project.app.";
            bind(p + "test");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void bind(String classpath) {
        try {

            Class c = Class.forName(classpath);

            for (Method m : c.getMethods()) {

                if (m.isAnnotationPresent(Web.class)) {
                    Handlers h = new UrlHandler(m);
                    hm.put("/apps/" + m.getAnnotation(Web.class).value(), new UrlHandler(m));
                }
            }
            System.out.println(hm.toString());

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
