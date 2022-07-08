package controllers;

import beans.TestBean;
import core.Satisfact;
import utils.CustomerContext;
import utils.JwtUtil;
import utils.PropertyUtil;


public class AuthSatisfact extends Satisfact {

    JwtUtil jwtUtil;

    public AuthSatisfact(CustomerContext context, PropertyUtil properties) {
        //jwtverify
        super(context, properties);


    }

    public TestBean aa()  {


        TestBean ret=new TestBean("a",5);
        return ret;
    }

    public String bb() {
        String name=request("name");
        String age=request("age");
        return "Calo App Info:"+getAppInfo()+name+age;
    }

    public TestBean cc()   {
        return new TestBean("calo",20);
    }


}
