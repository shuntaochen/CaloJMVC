package utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

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

            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                    .withJWTId("23")
                    .withClaim("username","chen")
                    .withClaim("age",18)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withIssuer("auth0")
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }


    public boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret"); //use more secure key
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            verifier.verify(token);
            return true;

        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            return false;
        }
    }

    public String decode(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuer();
        } catch (JWTDecodeException exception) {
            //Invalid token
        }
        return null;
    }

}
