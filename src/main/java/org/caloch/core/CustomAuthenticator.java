package org.caloch.core;


import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

public class CustomAuthenticator extends Authenticator {
    @Override
    public Result authenticate(HttpExchange exch) {
        String[] perms = new String[]{PermissionNames.GeneralRead};
        String realm = String.join("|", permlize(perms));
        return new Success(new HttpPrincipal("chen", realm));
//        return new Failure(500);
//        return new Retry(301);//503,429,
    }

    private String wrap(String src){
        return "["+src+"]";
    }
    private String[] permlize(String[] perms){
        for (int i = 0; i < perms.length; i++) {
            perms[i]=wrap(perms[i]);
        }
        return perms;
    }
}
