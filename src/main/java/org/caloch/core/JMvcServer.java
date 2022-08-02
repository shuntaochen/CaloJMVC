package org.caloch.core;

import com.sun.net.httpserver.*;
import org.caloch.utils.PropertyUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class JMvcServer {
    private String[] args;
    private boolean doAddDb;
    HttpServer server;

    public JMvcServer setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }
    Authenticator authenticator;
    PropertyUtil propertyUtil;

    public JMvcServer(PropertyUtil propertyUtil, String[] args) {
        this.args = args;
        this.propertyUtil = propertyUtil;
    }

    public void start() throws IOException {

        String portConfig = propertyUtil.getValue("port");
        int threadsCount = Integer.parseInt(propertyUtil.getValue("threadCount"));
        int connectionCount = Integer.parseInt(propertyUtil.getValue("connectionCount"));
        int port = (args.length == 0 ? Integer.valueOf(portConfig) : Integer.valueOf(args[0]));
        server = HttpServer.create(new InetSocketAddress(port), connectionCount);
        System.out.println("Customer system listening on:" + server.getAddress());
        server.setExecutor(Executors.newFixedThreadPool(threadsCount));
        HttpHandler handler = new JMvcHandler(propertyUtil, doAddDb);
        HttpContext ctxupload = server.createContext("/upload", new FileUpload());
//        server.createContext("/webapp", new ServerResourceHandler(
//                ServerConstant.SERVER_HOME + ServerConstant.FORWARD_SINGLE_SLASH, true, false));
        HttpContext ctx = server.createContext("/", handler);
        ctx.getFilters().add(new CustomFilter());//filter runs before handler,
        if (authenticator != null) {
            ctxupload.setAuthenticator(authenticator);
            ctx.setAuthenticator(authenticator);
        }
        server.start();
    }


    public JMvcServer addDbContext(boolean doAddDb) {
        this.doAddDb = doAddDb;
        return this;
    }
}
