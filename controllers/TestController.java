package controllers;

import java.io.IOException;

import beans.TestBean;
import com.sun.net.httpserver.HttpExchange;
import core.Controller;

public class TestController extends Controller {

    public TestController(HttpExchange exchange) {
        super(exchange);
        // TODO Auto-generated constructor stub
    }

    public void aa() throws IOException {
        OK("Hi aa");
    }

    public void bb() throws IOException {
        OK("Calo App Info:"+getAppInfo());
    }

    public void cc() throws Exception {
        Json(new TestBean("calo",20));
    }

}
