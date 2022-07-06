package controllers;

import core.SatisfyCustomer;
import utils.CustomerContext;
import utils.PropertyUtil;

import java.io.IOException;

public class FacadeSatisfyCustomer extends SatisfyCustomer {


    public FacadeSatisfyCustomer(CustomerContext exchange, PropertyUtil properties) {
        super(exchange, properties);
    }

    public void Home() throws IOException {
        OK("Hello world");
    }
}
