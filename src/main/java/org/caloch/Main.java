package org.caloch;


import org.caloch.core.*;


public class Main {
    public static void main(String[] args) throws Exception {
        new JMvcServer(args)
                .setAuthenticator(new CustomAuthenticator())
                .addDbContext(false)
                .start();
    }
}
