/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xagau.minepay;

//import com.xagau.atomiic.assets.Placeh;
import com.xagau.mq.Publish;
import com.xagau.mq.vtm.Transaction;
import com.xagau.notification.Messenger;
import com.xagau.notification.WebhookDirectory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sean
 */
public class Executor {

    Publish publish = new Publish();

    public static HashMap map = new HashMap();
    OutputStream os = null;
    InputStream is = null;
    BufferedReader br = null;

    Executor(OutputStream os, InputStream is, BufferedReader br) {
        this.os = os;
        this.is = is;
        this.br = br;
    }

    public void execute(Command c) {
        try {
            PrintWriter writer = new PrintWriter(os, true);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");

            if( c.getType().equals(CommandType.WITHDRAW)){
                System.out.println("placeh-cli sendtoaddress " + c.getAddress() + " " + c.getAmount());
                String who = c.getWho().replaceAll("\\[", "");
                who = who.replaceAll("\\]", "");
                who = who.replaceAll("<", "");
                who = who.replaceAll(">", "");
                writer.println("/data get entity " + who + " Inventory");
                String line = br.readLine();
                System.out.println("---WHO:" + who + "---");
                if (!line.contains("gold_ingot")) {
                    writer.println("/say " + who + " not enough gold!");
                    writer.flush();
                    return;
                } else {
                    System.out.println("---Inventory Listing:" + line);
                    int index = line.indexOf("gold_ingot");
                    String nLine = line.substring(index, line.length());
                    int goldcount = nLine.indexOf("Count:");
                    String eLine = nLine.substring(goldcount + "Count:".length(), nLine.indexOf("b"));
                    eLine = eLine.trim();
                    System.out.println("---Count:" + eLine + "---");
                    System.out.println("---Address:" + c.getAddress() + "---");
                    int goldCount = Integer.parseInt(eLine);

                    //Placeh placeh = new Placeh(); // FIX CACHING OF LIBS!!!!!!
                    if (c.getAddress() == null || !c.getAddress().startsWith("F") || c.getAddress().length() < 24 || c.getAddress().length() > 50) {
                        System.out.println("-say " + c.getAddress() + " invalid address to send to.");
                        writer.println("/say " + c.getAddress() + " invalid address to send to.");
                        writer.flush();
                        return;
                    }
                    if (goldCount < (int) c.getAmount()) {
                        System.out.println("-say " + who + " not enough gold!");
                        writer.println("/say " + who + " not enough gold!");
                        writer.flush();
                        return;
                    }
                }

                writer.flush();
                writer.println("/say " + who + " sent " + (int) Math.floor(c.getAmount()) + " to " + c.getAddress());
                writer.flush();
                writer.println("/clear " + who + " minecraft:gold_ingot " + (int) Math.floor(c.getAmount()));
                writer.flush();

                try { 
                    String wh = WebhookDirectory.getUrl("discord-placeholder-minecraft");
                    Messenger.notify(wh, "Player sent out payment", who + " sent " + (int) Math.floor(c.getAmount()) + " to " + c.getAddress());
                } catch(Exception ex) { 
                        ex.printStackTrace();
                }

                try {
                    Transaction t = new Transaction();
                    t.setCurrency("PHL");
                    t.setOtp(UUID.randomUUID().toString());
                    t.setClientId(who);
                    t.setTerminalId("706c616365682e696f");
                    t.setAmount(c.getAmount());
                    t.setReceipent(c.getAddress());
                    t.setTransactionId("MC" + System.currentTimeMillis());
                    publish.publish(t);

                } catch (Exception ex) {
                }
            
            } else {
                try { 
                    DecimalFormat df = new DecimalFormat("0");
                    String who = c.getWho();
                    String what = c.getWhat();
                    String wh = WebhookDirectory.getUrl("discord-placeholder-minecraft"); // FTfP6KAqifUgqRVNWXWrBGYCEBH27jRo3T
                    String msg = who + " purchased " + what + " for " + df.format(c.getAmount()) + " PHL waiting for coins @ [" + Server.getProperty("address") + "]";
                    String embed = "http://www.xagau.com/qrservlet?qrtext=" + Server.getProperty("address");
                    Messenger.notify(wh, "Player made purchase ", msg, embed );
                } catch(Exception ex) { 
                        ex.printStackTrace();
                }
            }

            //}
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
