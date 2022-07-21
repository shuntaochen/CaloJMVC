package org.caloch.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.caloch.beans.User;
import org.caloch.utils.*;
import org.caloch.core.*;

public class OpenSatisfact extends Satisfact {


    public OpenSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
        //todo: add db helper, maybe mysql,
    }

    public User ff() throws SQLException {
        User u = new User();
        u.setId(1);
        u.isDeleted = false;
        mysqlDbContext.update(u, "isdeleted");
        return u;

    }

    public void gg() throws IOException {
        redirect("https://baidu.com");
    }

    public User hh() throws SQLException {
        User u = new User();
        u.email = "359000081@qq.com";
        u.password = "cst";
        u.isDeleted = true;
        mysqlDbContext.insert(u);
        return u;
    }

    public User ii() throws SQLException {
        User query = new User();
        query.password = "cst";
        query.email = "359000081@qq.com";
        User u = mysqlDbContext.single(query);
        mysqlDbContext.delete(u);
        return u;
    }

    public ArrayList<User> jj() throws SQLException {
        User query = new User();
        ArrayList<User> u = mysqlDbContext.select(query);
        u=mysqlDbContext.selectPageAsc(query,"Id",1,1);
        u=mysqlDbContext.selectPageDesc(query,"id",1,1);

        return u;
    }


}
