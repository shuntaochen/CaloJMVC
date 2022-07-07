package controllers;

import beans.TestBean;
import core.Satisfact;
import utils.CustomerContext;
import utils.PropertyUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AuthSatisfact extends Satisfact {


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
