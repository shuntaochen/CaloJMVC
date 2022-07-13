package org.caloch.controllers;

import org.caloch.beans.*;
import org.caloch.beans.TestBean;
import org.caloch.utils.*;
import org.caloch.core.*;

import java.util.ArrayList;


//@Permission(name= PermissionNames.GeneralRead)
public class AuthSatisfact extends Satisfact {

    public AuthSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }


    public TestBean aa() throws Exception {
//        jwtFilter();
        ReflectionSqlBuilder rsb = new ReflectionSqlBuilder();
        TestBean ret = new TestBean("a", 5);
        ret = ReflectionSqlBuilder.inflate(new TestBean(), o -> request(o));
//        ret = ReflectionSqlBuilder.inflateNew(TestBean.class, o -> request(o));
        String sql = rsb.createInsertSql(ret);
        ArrayList<TestBean> r= new MysqlObjectHelper().select(ret);
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

    public TestBean dd() {
        return new TestBean("dd", 20);
    }

}
