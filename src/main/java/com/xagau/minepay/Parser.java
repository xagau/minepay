/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xagau.minepay;

import com.xagau.atomiic.assets.Placeh;
import com.xagau.notification.Messenger;
import com.xagau.notification.WebhookDirectory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
public class Parser {

    InputStream is = null;
    OutputStream os = null;
    BufferedReader reader = null;

    public Parser(OutputStream os, InputStream is, BufferedReader reader) {
        this.is = is;
        this.os = os;
        this.reader = reader;
    }

    public ArrayList find(String line) {

        try {
            if (line == null || line.isEmpty()) {
                return null;
            }

            ArrayList list = new ArrayList();
            Command c = new Command();
            c.setAddress("NA");
            c.setAmount(0);
            c.setTime(new Date(0));

            String splits[] = line.split(" ");
            ArrayList al = new ArrayList();

            System.out.println("parse line:" + line);

            if (line.contains("joined the game")) {
                String wh = WebhookDirectory.lookup("discord-placeholder-minecraft");
                Messenger.notify(wh, "Player Joined", line);
            }

            if (line.contains("withdraw")) {
                try {
                    String[] split = line.split(" ");
                    int frm = 0;
                    for (int k = 0; k <= split.length; k++) {
                        if (split[k].equals("withdraw")) {
                            frm = k;
                            break;
                        }
                    }

                    String who = split[frm - 1];
                    String cmd = split[frm];
                    String address = split[frm + 1];
                    String amount = split[frm + 2];

                    System.out.println("who:" + who);
                    System.out.println("cmd:" + cmd);
                    System.out.println("address:" + address);
                    System.out.println("amount:" + amount);
                    try {
                        c.setAmount(Double.parseDouble(amount));
                    } catch (Exception ex) {
                    }
                    c.setWho(who);
                    c.setAddress(address);
                    c.setType(CommandType.WITHDRAW);
                    SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:SS]");
                    c.setTime(new Date(System.currentTimeMillis()));

                    list.add(c);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (line.contains("deposit")) {

                try {
                    String[] split = line.split(" ");

                    int frm = 0;
                    for (int k = 0; k <= split.length; k++) {
                        if (split[k].equals("deposit")) {
                            frm = k;
                            break;
                        }
                    }

                    String who = split[frm - 1];
                    String cmd = split[frm];
                    String amount = split[frm + 1];
                    String what = split[frm + 2];

                    System.out.println("who:" + who);
                    System.out.println("cmd:" + cmd);
                    System.out.println("amount:" + amount);
                    System.out.println("what:" + what);

                    c.setWho(who);
                    c.setWhat(what);
                    c.setType(CommandType.DEPOSIT);
                    try {
                        c.setAmount(Double.parseDouble(amount));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    c.setTime(new Date(System.currentTimeMillis()));

                    list.add(c);
                } catch(Exception ex) {
                   ex.printStackTrace();
                } 
            }

            return list;
        } catch(Exception ex) { 
            ex.printStackTrace();
        }
        return null;
    }   
}

