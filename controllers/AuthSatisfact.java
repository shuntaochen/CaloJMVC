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

    public void aa() throws IOException, InvocationTargetException, IllegalAccessException {
        Object ret=new TestBean("a",5);
        json(ret);
    }

    public void bb() throws IOException {
        String name=request("name");
        String age=request("age");
        OK("Calo App Info:"+getAppInfo()+name+age);
    }

    public void cc() throws Exception {
        json(new TestBean("calo",20));
    }

    public void generateJwtToken() throws IOException {
        OK("");
    }

}
