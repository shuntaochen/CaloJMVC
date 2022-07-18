package org.caloch.core;


import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.util.ArrayList;

public class BasicAdminAuthenticator extends Authenticator {

    JwtUtil jwtUtil;

    public BasicAdminAuthenticator(PropertyUtil propertyUtil) {
        jwtUtil = new JwtUtil(propertyUtil);
    }

    @Override
    public Result authenticate(HttpExchange exchange) {//this should always succeed, just to set principle,then checkpermission,

//        String authToken=exchange.getRequestHeaders().get("authorization").get(0);
//        String issuer= jwtUtil.decode(authToken);


        String[] perms = new String[]{PermissionNames.BackofficeAdmin};
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
