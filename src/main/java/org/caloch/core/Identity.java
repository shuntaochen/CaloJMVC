package org.caloch.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Identity {
    public Identity(String name){
        this.setName(name);
    }
    private String name;

    private HashMap<String,Object> claims=new HashMap<>();
    private List<Integer> permissions=new ArrayList<>();

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> getClaims() {
        return claims;
    }


    public List<Integer> getPermissions() {
        return permissions;
    }
}
