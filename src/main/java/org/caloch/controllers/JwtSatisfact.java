package org.caloch.controllers;

import org.caloch.utils.*;
import org.caloch.core.*;


public class JwtSatisfact extends Satisfact {

    public JwtSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil, MySqlDbContext dbContext) {
        super(context, properties, jwtUtil, dbContext);
    }

    public String create() {
        String token = jwtUtil.create();
        return token;
    }

    public void verify() {
        String token = request("token");
        jwtUtil.verify(token);
    }

    public String decode() {
        String token = request("token");
        String audence = jwtUtil.decode(token);
        return audence;
    }
}
