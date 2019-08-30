package edu.escuelaing.arem.project.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author 2132219
 */
public class Browser {
    public static void main(String[] args) throws Exception {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            String st = bf.readLine();
            URL url = new URL(st);
            bf = new BufferedReader(new InputStreamReader(url.openStream()));
            File res = new File(System.getProperty("user.dir") + "/resultado.html");
            PrintWriter print = new PrintWriter(res);
            String s = bf.readLine();
            while (s != null) {
                print.write(s);
                s = bf.readLine();
            }
            print.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}