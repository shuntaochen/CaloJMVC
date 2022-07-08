import com.auth0.jwt.JWT;
import com.sun.net.httpserver.*;
import utils.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Executors;

import core.*;

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
        private JwtUtil jwtUtil;

        public CustomersHandler(PropertyUtil propertyUtil) {
            this.propertyUtil = propertyUtil;
            jwtUtil=new JwtUtil(propertyUtil);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                {
                    System.out.println("-- headers --");
                    Headers requestHeaders = exchange.getRequestHeaders();
                    requestHeaders.entrySet().forEach(System.out::println);

                    System.out.println("-- principle --");
                    HttpPrincipal principal = exchange.getPrincipal();
                    System.out.println(principal);

                    System.out.println("-- HTTP method --");
                    String requestMethod = exchange.getRequestMethod();
                    System.out.println(requestMethod);

                    System.out.println("-- query --");
                    URI requestURI = exchange.getRequestURI();
                    String query = requestURI.getQuery();
                    System.out.println(query);
                }

                CustomerContext helper = new CustomerContext(exchange);
                JsonHelper jsonHelper=new JsonHelper();
                String rootPath = exchange.getHttpContext().getPath();
                System.out.println(rootPath);
                URI requestUri = exchange.getRequestURI();
                String path=requestUri.getPath();
                String[] routeParts = path.substring(1).split("/");
                if(routeParts.length!=2)return;
                char ctlLeading = Character.toUpperCase(routeParts[0].charAt(0));
                Constructor<?>[] constructors = Class
                        .forName("controllers."+ctlLeading + routeParts[0].toLowerCase().substring(1) + "Satisfact")
                        .getConstructors();
                Satisfact ctrl = (Satisfact) constructors[0].newInstance(helper,propertyUtil,jwtUtil);
                String methodName= routeParts[1].toLowerCase();
                List<Method> methods= Arrays.asList(ctrl.getClass().getDeclaredMethods());
                Method m=getMethodName(methods,methodName);
                if(m==null){
                    throw new Exception("method not found");
                }
                Object ret= m.invoke(ctrl);
                String result="";
                if(!TypeChecker.isValueOrString(ret)){
                 result=jsonHelper.convertToJson(ret);
                }else
                {
                    result=ret.toString();
                }
                int code= exchange.getResponseCode();
                if(code==-1){
                    exchange.sendResponseHeaders(200, result.length());
                }
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();
                exchange.close();

            }
            catch (ClassNotFoundException cnfe){
                TerminateResponseWith500(exchange,cnfe.getMessage());
            }
            catch (InvocationTargetException e){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String message= e.getTargetException().getMessage()+ sw;
                TerminateResponseWith500(exchange,message);
            }
            catch (Exception e) {
                e.printStackTrace();
                TerminateResponseWith500(exchange,e.toString());
            }
            finally {

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
