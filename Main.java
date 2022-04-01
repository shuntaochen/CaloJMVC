import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import utils.ExchangeHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import utils.*;
import core.*;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println(args.length == 0);
        int port = (args.length == 0 ? 8001 : Integer.valueOf(args[0]));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Calo system listening on:"+server.getAddress());
        server.setExecutor( Executors.newFixedThreadPool(10));
        server.createContext("/", new TestHandler());
        server.start();
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                ExchangeHelper helper = new ExchangeHelper(exchange);
                String path = helper._exchange.getHttpContext().getPath();// "/"
                String requestUri = exchange.getRequestURI().toString();// test, activa controller
                System.out.println(requestUri);
                String[] routeParts = requestUri.substring(1).split("/");
                char ctlLeading = Character.toUpperCase(routeParts[0].charAt(0));
                Constructor<?> constructor = Class
                        .forName("controllers."+ctlLeading + routeParts[0].toLowerCase().substring(1) + "Controller")
                        .getConstructors()[0];
                Controller ctrl = (Controller) constructor.newInstance(exchange);
                Method method = ctrl.getClass().getDeclaredMethod(routeParts[1]);
                method.invoke(ctrl);

            }  catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

}
