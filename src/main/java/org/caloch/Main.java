package org.caloch;


import org.caloch.core.*;
import org.caloch.utils.PropertyUtil;


public class Main {
    public static void main(String[] args) throws Exception {

        String x= "Bear asd".split("Bear ")[0];
        PropertyUtil propertyUtil = new PropertyUtil();
        new JMvcServer(propertyUtil, args)
                .setAuthenticator(new BasicAdminAuthenticator(propertyUtil))
                .addDbContext(false)
                .start();
    }
}
