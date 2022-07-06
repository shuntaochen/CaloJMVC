package controllers;

import core.Satisfact;
import utils.CustomerContext;
import utils.PropertyUtil;

import java.io.IOException;

public class FacadeSatisfact extends Satisfact {


    public FacadeSatisfact(CustomerContext exchange, PropertyUtil properties) {
        super(exchange, properties);
    }

    public void Home() throws IOException {
        OK("Hello world");
    }
}
