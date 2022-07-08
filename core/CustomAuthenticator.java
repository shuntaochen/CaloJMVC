package core;


import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class CustomAuthenticator extends Authenticator {
    @Override
    public Result authenticate(HttpExchange exch) {
        return new Success(new HttpPrincipal("chen","calo"));
    }
}
