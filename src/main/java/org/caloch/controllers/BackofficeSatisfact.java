package org.caloch.controllers;

import org.caloch.core.Anonymous;
import org.caloch.core.Permission;
import org.caloch.core.PermissionNames;
import org.caloch.core.Satisfact;
import org.caloch.dtos.Login;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;


@Permission(name = PermissionNames.BackofficeAdmin)
public class BackofficeSatisfact extends Satisfact {
    public BackofficeSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }

    @Anonymous
    public String login() {
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
