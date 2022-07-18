package org.caloch.controllers;

import org.caloch.core.Satisfact;
import org.caloch.dtos.Login;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

public class LoginSatisfact extends Satisfact {
    public LoginSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }

    public String in() {
        Login dto = new Login();
        dto.name = request("name");
        dto.password = request("password");
        if (dto.name.equals("chen") && dto.password.equals("abcd")) {
            String token = jwtUtil.create();
            return token;
        }
        return "failure";
    }

}
