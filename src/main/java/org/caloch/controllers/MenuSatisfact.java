package org.caloch.controllers;

import org.caloch.beans.MenuItem;
import org.caloch.core.Satisfact;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.util.ArrayList;

public class MenuSatisfact extends Satisfact {

    public MenuSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }

    public ArrayList<MenuItem> getAll() {
        ArrayList<MenuItem> ret = new ArrayList<>();

        {
            MenuItem m = new MenuItem();
            m.id = 1;
            m.pid = -1;
            m.name = "root";
            ret.add(m);
        }

        {
            MenuItem m = new MenuItem();
            m.id = 2;
            m.pid = 1;
            m.name = "lv1";
            ret.add(m);
        }

        {
            MenuItem m = new MenuItem();
            m.id = 3;
            m.pid = 1;
            m.name = "lv11";
            ret.add(m);
        }
        return ret;
    }
}
