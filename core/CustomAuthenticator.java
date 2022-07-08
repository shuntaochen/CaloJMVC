package core;


import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class CustomAuthenticator extends Authenticator {
    @Override
    public Result authenticate(HttpExchange exch) {
        String[] perms = new String[]{PermissionNames.GeneralRead, PermissionNames.GeneralWrite};
        String realm = String.join("|", "[" + perms + "]");
        return new Success(new HttpPrincipal("chen", "calo"));
//        return new Failure(500);
//        return new Retry(301);//503,429,
    }
}
