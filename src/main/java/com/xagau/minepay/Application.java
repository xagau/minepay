/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xagau.minepay;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sean
 */
public class Application {

    public static void main(String[] args) {

        try {
            System.out.println("Version 1.16.3 XVV minepay by xagau (Placeholders)");
                    
            Process p = Runtime.getRuntime().exec("java -Xms12024M -Xmx15048M -jar server.jar nogui");
            OutputStream os = p.getOutputStream();
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            boolean running = true;
            Parser parser = new Parser(os,is,reader);
            Executor executor = new Executor(os, is, reader);
            while (running == true) {
                try {
                    String line = reader.readLine();
                    System.out.println("MinePay Server:" + line);
                    ArrayList list = parser.find(line);
                    if( list != null ) { 
                        for (int i = 0; i < list.size(); i++) {
                            Command c = (Command) list.get(i);
                            executor.execute(c);
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                } catch(Exception ex) { 
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
