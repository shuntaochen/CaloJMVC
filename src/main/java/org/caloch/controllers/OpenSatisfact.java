package org.caloch.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.mchange.v2.beans.swing.TestBean;
import org.caloch.beans.User;
import org.caloch.utils.*;
import org.caloch.core.*;

public class OpenSatisfact extends Satisfact {


    public OpenSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
        //todo: add db helper, maybe mysql,
    }
    public HashMap<String, Integer> ff() {
        HashMap<String, Integer> ret = new HashMap<>();
        ret.put("name", 22);
        ret.put("age", 23);
        boolean b = TypeChecker.isMap(ret);
        Class<?> t = ret.getClass();
        return ret;
    }

    public void gg() throws IOException {
        redirect("https://baidu.com");
    }

    public User hh() throws SQLException {
        User u=new User();
        u.email="359000081@qq.com";
        u.password="cst";
        mySqlDbContext.insert(u);
        return u;
    }

    public User ii() throws SQLException {
        User query=new User();
        query.email="359000081@qq.com";
        User u=mySqlDbContext.single(query);
        return u;
    }


}
