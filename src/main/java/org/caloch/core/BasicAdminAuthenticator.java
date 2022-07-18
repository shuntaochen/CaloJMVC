package org.caloch.core;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Header;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.util.ArrayList;
import java.util.List;

public class BasicAdminAuthenticator extends Authenticator {

    JwtUtil jwtUtil;

    public BasicAdminAuthenticator(PropertyUtil propertyUtil) {
        jwtUtil = new JwtUtil(propertyUtil);
    }

    @Override
    public Result authenticate(HttpExchange exchange) {//this should always succeed, just to set principle,then checkpermission,
        String permissions = "";
        String user="chen";
        if (exchange.getRequestHeaders().containsKey("authorization")) {
            String authToken = exchange.getRequestHeaders().get("authorization").get(0);
            String token = authToken.split("Bearer ")[1];
            DecodedJWT jwt = jwtUtil.decodeJwt(token);
            permissions = jwt.getClaims().get("permission").toString();
            user = jwt.getClaims().get("username").toString();
        }
        JMvcPrinciple principal = new JMvcPrinciple(user, permissions);
        ArrayList<String> feMenu = new ArrayList<>();
        ArrayList<String> boMenu = new ArrayList<>();
        principal.setFeMenu(feMenu);
        principal.setBoMenu(boMenu);
        return new Success(principal);
//        return new Failure(500);
//        return new Retry(301);//503,429,
    }


}
