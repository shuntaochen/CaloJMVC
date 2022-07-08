package controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import core.Satisfact;
import utils.CustomerContext;
import utils.JwtUtil;
import utils.PropertyUtil;

public class JwtSatisfact extends Satisfact {

    public JwtSatisfact(CustomerContext context, PropertyUtil properties,JwtUtil jwtUtil) {
        super(context, properties,jwtUtil);
    }

    public String create(){
        String token=jwtUtil.create();
        return token;
    }

    public boolean verify(){
        String token=request("token");
        return jwtUtil.verify(token);
    }

    public String decode(){
        String token=request("token");
        String audence= jwtUtil.decode(token);
        return audence;
    }
}
