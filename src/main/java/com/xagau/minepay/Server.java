/** Copyright (c) 2018 placeh.io,
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author xagau
 *
 */
package com.xagau.minepay;

import java.rmi.*;

import java.rmi.registry.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Server {

    int port;
    String address;
    Registry registry = null;    // rmi registry for lookup the remote objects.

    static boolean first = true;
    static Properties p = new Properties();
    

    public static String getProperty(String property) {

        try {
            if (first) {
                File f = new File(Globals.minecraftPath + "/server.properties");
                FileInputStream fis = new FileInputStream(f);
                p.load(fis);
                fis.close();
                first = false;
            }

            String prop = p.getProperty(property);

            return prop;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "<NA>";
    }

    public Server() {
        // get the address of this host.
    }
}
