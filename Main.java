import beans.TestBean;
import beans.TestBean1;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import utils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import core.*;

public class Main {

    public static void main(String[] args) throws Exception {


        new TypeChecker().Check();
        String json= new JsonHelper().convertToJson(new TestBean1("5"));
        String json1= new JsonHelper().convertToJson(new TestBean("5",3));


        PropertyUtil propertyUtil=new PropertyUtil();
        String portConfig= propertyUtil.getValue("port");
        int port = (args.length == 0 ? Integer.valueOf(portConfig) : Integer.valueOf(args[0]));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("Calo system listening on:"+server.getAddress());
        server.setExecutor( Executors.newFixedThreadPool(10));
        server.createContext("/", new CaloHandler(propertyUtil));
        server.start();
    }

    static class CaloHandler implements HttpHandler {
        private PropertyUtil propertyUtil;

        public CaloHandler(PropertyUtil propertyUtil) {
            this.propertyUtil = propertyUtil;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                ExchangeHelper helper = new ExchangeHelper(exchange);
                String path = helper._exchange.getHttpContext().getPath();// "/"
                String requestUri = exchange.getRequestURI().toString();// test, activa controller
                System.out.println(requestUri);
                String[] routeParts = requestUri.substring(1).split("/");
                char ctlLeading = Character.toUpperCase(routeParts[0].charAt(0));
                Constructor<?>[] constructors = Class
                        .forName("controllers."+ctlLeading + routeParts[0].toLowerCase().substring(1) + "Controller")
                        .getConstructors();
                Controller ctrl = (Controller) constructors[0].newInstance(helper,propertyUtil);
                int questionIndex=routeParts[1].indexOf("?");
                Method method = ctrl.getClass().getDeclaredMethod(routeParts[1].substring(0,questionIndex!=-1?questionIndex:routeParts[1].length()));
                method.invoke(ctrl);

            }catch (ClassNotFoundException cnfe){

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
