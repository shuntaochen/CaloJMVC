package org.caloch.controllers;

import org.caloch.beans.Roles;
import org.caloch.beans.TestBean;
import org.caloch.utils.*;
import org.caloch.core.*;

import java.sql.SQLException;
import java.util.ArrayList;


//@Permission(name= PermissionNames.GeneralRead)
public class AuthSatisfact extends Satisfact {

    public AuthSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil,MySqlDbContext dbContext) {
        super(context, properties, jwtUtil, dbContext);
    }


    public TestBean aa() throws Exception {
//        jwtFilter();
        ReflectionSqlBuilder rsb = new ReflectionSqlBuilder();
        TestBean ret = new TestBean("a", 5);
        ret = ReflectionSqlBuilder.inflate(new TestBean(), o -> request(o));
//        ret = ReflectionSqlBuilder.inflateNew(TestBean.class, o -> request(o));
        String sql = rsb.createInsertSql(ret);
        ArrayList<TestBean> r= new MySqlDbContext().select(ret);
        return ret;
    }

    @Anonymous
    @Permission(name = PermissionNames.GeneralWrite)
    public String bb() {
        String name = request("name");
        String age = request("age");
        return "Calo App Info:" + getAppInfo() + name + age;
    }

    @Permission(name = PermissionNames.GeneralWrite)
    public TestBean cc() {
        return new TestBean("calo", 20);
    }

    public TestBean dd() throws SQLException {
        MySqlDbContext ctx=new MySqlDbContext();
        ctx.connect();
        Roles r=new Roles();
        r.setName("chen");
        ctx.insert(r);
        ctx.commit();
        return new TestBean("dd", 20);
    }

}
