package org.caloch.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.caloch.core.PermissionNames;

import java.util.Date;

public class JwtUtil {


    private PropertyUtil propertyUtil;

    public JwtUtil(PropertyUtil propertyUtil) {

        this.propertyUtil = propertyUtil;
    }

    /*

     */
    public String create() {
        try {
            String[] perms = new String[]{PermissionNames.BackofficeAdmin};
            String realm = String.join("|", permlize(perms));
            String jwtIssuer = propertyUtil.getValue("jwt");
            String jwtSecret = propertyUtil.getValue("jwtSecret");
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            String token = JWT.create()
//                    .withJWTId("23")
                    .withClaim("permission", realm)
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

    public DecodedJWT decodeJwt(String token) {
        return JWT.decode(token);
    }

    public String decodeIssuer(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            jwt.getClaims();
            return jwt.getIssuer();
        } catch (JWTDecodeException exception) {
            //Invalid token
        }
        return null;
    }

    private String[] permlize(String[] perms) {
        for (int i = 0; i < perms.length; i++) {
            perms[i] = wrap(perms[i]);
        }
        return perms;
    }

    private String wrap(String src) {
        return "[" + src + "]";
    }

}
