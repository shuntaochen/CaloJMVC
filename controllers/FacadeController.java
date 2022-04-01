package controllers;

import com.sun.net.httpserver.HttpExchange;
import core.Controller;

import java.io.IOException;

public class FacadeController extends Controller {
    public FacadeController(HttpExchange exchange) {
        super(exchange);
    }

    public void Home() throws IOException {
        OK("Hello world");
    }
}
