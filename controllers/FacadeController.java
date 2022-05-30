package controllers;

import core.Controller;
import utils.HttpContext;
import utils.PropertyUtil;

import java.io.IOException;

public class FacadeController extends Controller {


    public FacadeController(HttpContext exchange, PropertyUtil properties) {
        super(exchange, properties);
    }

    public void Home() throws IOException {
        OK("Hello world");
    }
}
