package org.caloch.core;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class CustomFilter extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String path= exchange.getRequestURI().getPath();//ignore .favoricon.
        System.out.println(path);
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return null;
    }
}
