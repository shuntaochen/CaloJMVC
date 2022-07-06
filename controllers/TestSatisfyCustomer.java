package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import beans.TestBean;

import core.SatisfyCustomer;
import utils.CustomerContext;
import utils.PropertyUtil;

public class TestSatisfyCustomer extends SatisfyCustomer {


    public TestSatisfyCustomer(CustomerContext context, PropertyUtil properties) {
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
