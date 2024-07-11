/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xagau.minepay;

/**
 *
 * @author Sean
 */
public class Globals {
    public static String minecraftPath = "."; //"/home/pi/minecraft";
    public static String minecraftLogFilePath = Server.getProperty("minecraft.log.path")==null?"/home/pi/minecraft/logs/latest.log":Server.getProperty("minecraft.log.path");
}
