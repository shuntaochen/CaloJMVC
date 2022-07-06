import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import utils.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

import core.*;

import javax.net.ssl.HttpsURLConnection;

public class Main {

    public static void main(String[] args) throws Exception {
        PropertyUtil propertyUtil=new PropertyUtil();
        String portConfig= propertyUtil.getValue("port");
        int port = (args.length == 0 ? Integer.valueOf(portConfig) : Integer.valueOf(args[0]));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Customer system listening on:"+server.getAddress());
        server.setExecutor( Executors.newFixedThreadPool(100));
        server.createContext("/", new CustomersHandler(propertyUtil));
        server.start();
    }

    static class CustomersHandler implements HttpHandler {
        private PropertyUtil propertyUtil;

        public CustomersHandler(PropertyUtil propertyUtil) {
            this.propertyUtil = propertyUtil;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                CustomerContext helper = new CustomerContext(exchange);
                String path = exchange.getHttpContext().getPath();
                String requestUri = exchange.getRequestURI().toString();
                System.out.println(requestUri);
                String[] routeParts = requestUri.substring(1).split("/");
                if(routeParts.length!=2)return;
                char ctlLeading = Character.toUpperCase(routeParts[0].charAt(0));
                Constructor<?>[] constructors = Class
                        .forName("controllers."+ctlLeading + routeParts[0].toLowerCase().substring(1) + "SatisfyCustomer")
                        .getConstructors();
                SatisfyCustomer ctrl = (SatisfyCustomer) constructors[0].newInstance(helper,propertyUtil);
                int questionIndex=routeParts[1].indexOf("?");
                String methodName= routeParts[1].substring(0,questionIndex!=-1?questionIndex:routeParts[1].length()).toLowerCase();
                List<Method> methods= Arrays.asList(ctrl.getClass().getDeclaredMethods());
                Method m=getMethodName(methods,methodName);
                if(m==null){
                    throw new Exception("method not found");
                }
                m.invoke(ctrl);
            }
            catch (ClassNotFoundException cnfe){
                TerminateResponseWith500(exchange,cnfe.getMessage());
            }
            catch (Exception e) {
                e.printStackTrace();
                TerminateResponseWith500(exchange,e.getMessage());
            }
        }

        private Method getMethodName(List<Method> methods, String methodName){
            for (Method m:methods
            ) {
                String name=m.getName().toLowerCase();
                boolean found= name.equals(methodName);
                if(found){
                    return m;
                }
                else{
                    System.out.println(name);
                }
            }
            return null;
        }
        public void TerminateResponseWith500(HttpExchange exchange,String message) throws IOException {
            exchange.sendResponseHeaders(500, message.length());
            exchange.getResponseBody().write(message.getBytes());
            exchange.getResponseBody().close();
            exchange.close();

        }
    }

}
