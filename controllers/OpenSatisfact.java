package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import beans.TestBean;

import core.Satisfact;
import utils.CustomerContext;
import utils.PropertyUtil;
import utils.TypeChecker;

public class OpenSatisfact extends Satisfact {


    public OpenSatisfact(CustomerContext context, PropertyUtil properties) {
        super(context, properties);
    }

    public TestBean aa() throws Exception {
        HashMap<String,Integer> ret1= new HashMap<>();
        ret1.put("name",22);
        ret1.put("age",23);
        TestBean ret=new TestBean("a",5);
        ret.setBMap(ret1);
        return ret;
    }

    public String bb()  {
        String name=request("name");
        String age=request("age");
        return "Calo App Info:"+getAppInfo()+name+age;
    }

    public TestBean cc()  {
        return new TestBean("calo",20);
    }

    public void upload() throws Exception {
        throw new Exception("fd");
    }

    public int[] dd(){
        return new int[]{2,3,4};
    }

    public TestBean[] ee(){
        return new TestBean[]{
                new TestBean("calo",21),
                new TestBean("chen",22),
        };
    }

    public HashMap<String,Integer> ff(){
        HashMap<String,Integer> ret= new HashMap<>();
        ret.put("name",22);
        ret.put("age",23);
        boolean b= TypeChecker.isMap(ret);
        Class<?> t=ret.getClass();
        return ret;
    }


}
