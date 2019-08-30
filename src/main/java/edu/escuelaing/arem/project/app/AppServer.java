package edu.escuelaing.arem.project.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class AppServer {
    public static void escuchar() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
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

    public static void initicializar() {
        try {
            Class c = Class.forName("edu.escuelaing.arem.project.app.Browser");
            Method main = c.getDeclaredMethod("prueba", null);
            System.out.format("invoking %s.prueba()%n", c.getName());
            main.invoke(null, null);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}