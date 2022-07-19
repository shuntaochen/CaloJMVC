package org.caloch;


import org.caloch.core.*;
import org.caloch.utils.PropertyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) throws Exception {
        PropertyUtil propertyUtil = new PropertyUtil();
        new JMvcServer(propertyUtil, args)
                .setAuthenticator(new BasicAdminAuthenticator(propertyUtil))
                .addDbContext(false)
                .start();


    }
}
