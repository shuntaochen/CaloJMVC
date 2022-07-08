package controllers;

import beans.TestBean;
import com.sun.net.httpserver.HttpPrincipal;
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
    @Permission(name= PermissionNames.GeneralWrite)
    public String bb() {
        String name = request("name");
        String age = request("age");
        return "Calo App Info:" + getAppInfo() + name + age;
    }

    public TestBean cc() {
        return new TestBean("calo", 20);
    }


}
