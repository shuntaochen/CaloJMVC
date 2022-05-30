package controllers;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import beans.TestBean;

import core.Controller;
import utils.HttpContext;
import utils.PropertyUtil;
import org.json.simple.*;

public class TestController extends Controller {


    public TestController(HttpContext exchange, PropertyUtil properties) {
        super(exchange, properties);
    }

    public void aa() throws IOException {
        Object json=new TestBean("a",5);

        OK(json.toString());
    }

    public void bb() throws IOException {
        String name=request("name");
        String age=request("age");
        OK("Calo App Info:"+getAppInfo()+name+age);
    }

    public void cc() throws Exception {
        Json(new TestBean("calo",20));
    }

    public void generateJwtToken(){

    }

}
