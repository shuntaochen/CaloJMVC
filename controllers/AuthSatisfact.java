package controllers;

import beans.TestBean;
import core.Anonymous;
import core.Permission;
import core.PermissionNames;
import core.Satisfact;
import utils.CustomerContext;
import utils.JwtUtil;
import utils.PropertyUtil;


//@Permission(name= PermissionNames.GeneralRead)
public class AuthSatisfact extends Satisfact {

    public AuthSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }


    public TestBean aa() throws Exception {
        jwtFilter();
        TestBean ret = new TestBean("a", 5);
        return ret;
    }
    @Anonymous
    @Permission(name= PermissionNames.GeneralWrite)
    public String bb() {
        String name = request("name");
        String age = request("age");
        return "Calo App Info:" + getAppInfo() + name + age;
    }

    @Permission(name= PermissionNames.GeneralWrite)
    public TestBean cc() {
        return new TestBean("calo", 20);
    }

    public TestBean dd() {
        return new TestBean("dd", 20);
    }

}
