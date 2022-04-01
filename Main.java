import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {
        JsonHelper j=new JsonHelper();
        TestBean t=new TestBean();
        t.setAge(20);
        t.setName("Calo");
        String json=j.convertToJson(t);

        System.out.println("Hello world");
        System.out.println(args.length == 0);
        int port = (args.length == 0 ? 8001 : Integer.valueOf(args[0]));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
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
                        .forName(ctlLeading + routeParts[0].substring(1) + "Controller")
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
