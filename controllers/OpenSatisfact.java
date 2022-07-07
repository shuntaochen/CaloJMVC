package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import beans.TestBean;

import core.Satisfact;
import utils.CustomerContext;
import utils.PropertyUtil;

public class OpenSatisfact extends Satisfact {


    public OpenSatisfact(CustomerContext context, PropertyUtil properties) {
        super(context, properties);
    }

    public TestBean aa() throws Exception {
        TestBean ret=new TestBean("a",5);

        throw new Exception("ss");
//        return ret;
    }

    public String bb()  {
        String name=request("name");
        String age=request("age");
        return "Calo App Info:"+getAppInfo()+name+age;
    }

    public TestBean cc()  {
        return new TestBean("calo",20);
    }


}
