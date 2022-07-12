package org.caloch.core;


import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;

public class CustomAuthenticator extends Authenticator {
    @Override
    public Result authenticate(HttpExchange exch) {
        String[] perms = new String[]{PermissionNames.GeneralRead};
        String realm = String.join("|", permlize(perms));
        JMvcPrinciple principal = new JMvcPrinciple("chen", realm);
        ArrayList<String> feMenu = new ArrayList<>();
        ArrayList<String> boMenu = new ArrayList<>();
        principal.setFeMenu(feMenu);
        principal.setBoMenu(boMenu);
        return new Success(principal);
//        return new Failure(500);
//        return new Retry(301);//503,429,
    }

    private String wrap(String src) {
        return "[" + src + "]";
    }

    private String[] permlize(String[] perms) {
        for (int i = 0; i < perms.length; i++) {
            perms[i] = wrap(perms[i]);
        }
        return perms;
    }
}
