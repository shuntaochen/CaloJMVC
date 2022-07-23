package org.caloch.controllers;

import org.caloch.beans.Registration;
import org.caloch.core.*;
import org.caloch.dtos.Login;
import org.caloch.dtos.RegistrationDto;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;
import org.caloch.utils.ReflectionHelper;

import java.sql.SQLException;


@Permission(name = PermissionNames.BackofficeAdmin)
public class BackofficeSatisfact extends Satisfact {
    public BackofficeSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }

    @Anonymous
    public JsonResult login() throws SQLException {
        Registration r = inflateNew(Registration.class);
        Registration r1 = mysqlDbContext.single(r);
        if (r1 != null) {
            String token = jwtUtil.create();
            return JsonResult.ok(token);
        }
        return JsonResult.fail();
    }

    @Anonymous
    public JsonResult register() throws SQLException {
        Registration r = inflateNew(Registration.class);
        mysqlDbContext.insert(r);
        return JsonResult.ok();
    }

    public JsonResult getUserInfo() throws SQLException {
        Registration r = new Registration();
        r.email = request("email");
        Registration r1 = mysqlDbContext.single(r);
        return JsonResult.ok(r1);
    }

}
