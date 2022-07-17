package org.caloch.core;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.caloch.utils.PropertyUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class JMvcServer {
    private String[] args;
    private boolean doAddDb;
    HttpServer server;

    public JMvcServer(String[] args) {

        this.args = args;
    }

    public void start() throws IOException {
        PropertyUtil propertyUtil = new PropertyUtil();
        String portConfig = propertyUtil.getValue("port");
        int threadsCount = Integer.parseInt(propertyUtil.getValue("threadCount"));
        int connectionCount = Integer.parseInt(propertyUtil.getValue("connectionCount"));
        int port = (args.length == 0 ? Integer.valueOf(portConfig) : Integer.valueOf(args[0]));
        server = HttpServer.create(new InetSocketAddress(port), connectionCount);


        System.out.println("Customer system listening on:" + server.getAddress());
        server.setExecutor(Executors.newFixedThreadPool(threadsCount));
        HttpHandler handler = new CustomersHandler(propertyUtil, doAddDb);
        server.createContext("/webapp", new ServerResourceHandler(
                ServerConstant.SERVER_HOME + ServerConstant.FORWARD_SINGLE_SLASH + ServerConstant.WEBAPP_DIR, true, false));
        HttpContext ctx = server.createContext("/", handler);

        ctx.getFilters().add(new CustomFilter());//filter runs before handler,
        ctx.setAuthenticator(new CustomAuthenticator());
        server.start();
    }

    public JMvcServer addDbContext(boolean doAddDb) {
        this.doAddDb = doAddDb;
        return this;
    }
}
