package org.caloch.controllers;

import com.mchange.v2.beans.swing.TestBean;
import org.caloch.beans.Roles;
import org.caloch.utils.*;
import org.caloch.core.*;

import java.sql.SQLException;
import java.util.ArrayList;


//@Permission(name= PermissionNames.GeneralRead)
public class AuthSatisfact extends Satisfact {

    public AuthSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }


}
