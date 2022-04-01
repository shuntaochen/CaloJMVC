package controllers;

import java.io.IOException;

import beans.TestBean;
import com.sun.net.httpserver.HttpExchange;
import core.Controller;
import utils.ExchangeHelper;
import utils.PropertyUtil;

public class TestController extends Controller {


    public TestController(ExchangeHelper exchange, PropertyUtil properties) {
        super(exchange, properties);
    }

    public void aa() throws IOException {
        OK("Hi aa");
    }

    public void bb() throws IOException {
        String name=request("name");
        String age=request("age");
        OK("Calo App Info:"+getAppInfo()+name+age);
    }

    public void cc() throws Exception {
        Json(new TestBean("calo",20));
    }

}
