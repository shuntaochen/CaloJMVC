package controllers;

import com.sun.net.httpserver.HttpExchange;
import core.Controller;
import utils.ExchangeHelper;
import utils.PropertyUtil;

import java.io.IOException;

public class FacadeController extends Controller {


    public FacadeController(ExchangeHelper exchange, PropertyUtil properties) {
        super(exchange, properties);
    }

    public void Home() throws IOException {
        OK("Hello world");
    }
}
