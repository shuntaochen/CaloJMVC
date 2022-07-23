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
        Registration r1 = new Registration();
        r1.email = r.email;
        Registration r2 = mysqlDbContext.single(r1);
        if (r2 != null) return JsonResult.fail("email already exists");
        mysqlDbContext.insert(r);
        return JsonResult.ok(jwtUtil.create());
    }

    public JsonResult getUserInfo() throws SQLException {
        String err = "";
        Registration r = new Registration();
        r.email = request("email");
        if (!InputValidator.isEmail(r.email)) err += "email address no correct;";
        if (!err.equals("")) return JsonResult.fail(err);
        Registration r1 = mysqlDbContext.single(r);
        return JsonResult.ok(r1);
    }

}
