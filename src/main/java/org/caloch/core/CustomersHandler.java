package org.caloch.core;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;
import org.caloch.utils.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CustomersHandler implements HttpHandler {
    private PropertyUtil propertyUtil;
    private JwtUtil jwtUtil;

    private MySqlDbContext mySqlDbContext;

    public CustomersHandler(PropertyUtil propertyUtil, boolean doAddDb) {
        this.propertyUtil = propertyUtil;
        jwtUtil = new JwtUtil(propertyUtil);
        if (doAddDb) {
            String dbUrl = propertyUtil.getValue("db");
            String dbUsername = propertyUtil.getValue("dbuser");
            String dbPassword = propertyUtil.getValue("dbpassword");
            this.mySqlDbContext = new MySqlDbContext(dbUrl, dbUsername, dbPassword);
            mySqlDbContext.connect();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            URI requestUri = exchange.getRequestURI();
            String path = requestUri.getPath();
            if (path.equals("/favicon.ico")) return;
            String realm = exchange.getPrincipal().getRealm();
            CustomerContext helper = new CustomerContext(exchange);
            JsonHelper jsonHelper = new JsonHelper();
            String rootPath = exchange.getHttpContext().getPath();
            System.out.println(rootPath);
            String[] routeParts = path.substring(1).split("/");
            if (routeParts.length != 2) return;
            char ctlLeading = Character.toUpperCase(routeParts[0].charAt(0));
            Constructor<?>[] constructors = Class
                    .forName("org.caloch.controllers." + ctlLeading + routeParts[0].toLowerCase().substring(1) + "Satisfact")
                    .getConstructors();
            Satisfact ctrl = (Satisfact) constructors[0].newInstance(helper, propertyUtil, jwtUtil);
            if (mySqlDbContext != null)
                ctrl.setDbContext(mySqlDbContext);
            checkPermission(ctrl.getClass(), realm);
            String methodName = routeParts[1].toLowerCase();
            List<Method> methods = Arrays.asList(ctrl.getClass().getDeclaredMethods());
            Method m = getMethodName(methods, methodName);
            checkPermission(m, realm);
            if (m == null) {
                throw new Exception("method not found");
            }
            Object ret = m.invoke(ctrl);
            String result = "";
            if (!TypeChecker.isValueOrString(ret)) {
                result = jsonHelper.convertToJson(ret);
            } else {
                result = ret.toString();
            }
            new ResultFilter(exchange);
            if (mySqlDbContext != null)
                mySqlDbContext.commit();
            int code = exchange.getResponseCode();
            if (code == -1) {
                exchange.sendResponseHeaders(200, result.length());
            }
            exchange.getResponseBody().write(result.getBytes());
            exchange.getResponseBody().close();
            exchange.close();

        } catch (ClassNotFoundException cnfe) {
            TerminateResponseWith500(exchange, cnfe.getMessage());
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String message = e.getTargetException().getMessage() + sw;
            TerminateResponseWith500(exchange, message);
        } catch (SQLException se) {
            try {
                if (mySqlDbContext != null)
                    mySqlDbContext.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TerminateResponseWith500(exchange, e.toString());
        } finally {
        }
    }

    private void checkPermission(Object src, String realm) throws Exception {
        if (src != null) {
            Anonymous am = src.getClass() == Method.class ? ((Method) src).getDeclaredAnnotation(Anonymous.class) : ((Class<?>) src).getDeclaredAnnotation(Anonymous.class);
            if (am != null) return;
            Permission perm1 = src.getClass() == Method.class ? ((Method) src).getDeclaredAnnotation(Permission.class) : ((Class<?>) src).getDeclaredAnnotation(Permission.class);
            if (perm1 != null) {
                String pname1 = perm1.name();
                if (realm.indexOf("[" + pname1 + "]") == -1) {
                    throw new Exception("No Permission");
                }
            }
        }
    }

    private Method getMethodName(List<Method> methods, String methodName) {
        for (Method m : methods
        ) {
            String name = m.getName().toLowerCase();
            boolean found = name.equals(methodName);
            if (found) {
                return m;
            } else {
                System.out.println(name);
            }
        }
        return null;
    }

    public void TerminateResponseWith500(HttpExchange exchange, String message) throws IOException {
        exchange.sendResponseHeaders(500, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
        exchange.close();

    }
}
