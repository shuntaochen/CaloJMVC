package org.caloch.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.caloch.core.PermissionNames;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {


    private PropertyUtil propertyUtil;

    public JwtUtil(PropertyUtil propertyUtil) {

        this.propertyUtil = propertyUtil;
    }

    /*

     */
    public String create() {
        try {
            String jwtIssuer = propertyUtil.getValue("jwt");
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
//                    .withJWTId("23")
                    .withClaim("permission", PermissionNames.BackofficeAdmin)
                    .withClaim("username", "chen")
                    .withClaim("age", 18)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withIssuer(jwtIssuer)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }


    public void verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret"); //use more secure key
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build(); //Reusable verifier instance
        verifier.verify(token);
    }

    public String decode(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            jwt.getClaims();
            return jwt.getIssuer();
        } catch (JWTDecodeException exception) {
            //Invalid token
        }
        return null;
    }

}
