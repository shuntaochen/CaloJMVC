package org.caloch.core;

import com.sun.net.httpserver.HttpPrincipal;

import java.util.List;

public class JMvcPrinciple extends HttpPrincipal {
    /**
     * Creates a {@code HttpPrincipal} from the given {@code username} and
     * {@code realm}.
     *
     * @param username the email of the user within the realm
     * @param realm    the realm for this user
     * @throws NullPointerException if either username or realm are {@code null}
     */
    public JMvcPrinciple(String username, String realm) {
        super(username, realm);
    }

    private List<String> feMenu;
    private List<String> boMenu;

    public List<String> getFeMenu() {
        return feMenu;
    }

    public void setFeMenu(List<String> feMenu) {
        this.feMenu = feMenu;
    }


    public List<String> getBoMenu() {
        return boMenu;
    }

    public void setBoMenu(List<String> boMenu) {
        this.boMenu = boMenu;
    }
}
