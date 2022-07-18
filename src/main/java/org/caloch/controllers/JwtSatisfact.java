package org.caloch.controllers;

import org.caloch.utils.*;
import org.caloch.core.*;


@Permission(name = PermissionNames.BackofficeAdmin)
public class JwtSatisfact extends Satisfact {

    public JwtSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
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
        String audence = jwtUtil.decodeIssuer(token);
        return audence;
    }
}
