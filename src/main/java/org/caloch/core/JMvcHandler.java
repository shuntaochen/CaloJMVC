package org.caloch.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;
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

public class JMvcHandler implements HttpHandler {
    static Logger logger = Logger.getLogger(JMvcHandler.class);
    private PropertyUtil propertyUtil;
    private JwtUtil jwtUtil;

    private MySqlDbContext mySqlDbContext;

    public JMvcHandler(PropertyUtil propertyUtil, boolean doAddDb) {
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
            String[] routeParts = path.substring(1).split("/");
            if (routeParts.length != 2) return;
            char ctlLeading = Character.toUpperCase(routeParts[0].charAt(0));
            Constructor<?>[] constructors = Class
                    .forName("org.caloch.controllers." + ctlLeading + routeParts[0].toLowerCase().substring(1) + "Satisfact")
                    .getConstructors();
            Satisfact ctrl = (Satisfact) constructors[0].newInstance(helper, propertyUtil, jwtUtil);
            if (mySqlDbContext != null)
                ctrl.setDbContext(mySqlDbContext);
            String methodName = routeParts[1].toLowerCase();
            List<Method> methods = Arrays.asList(ctrl.getClass().getMethods());
            Method m = getMethodName(methods, methodName);
            checkPermission(ctrl.getClass(), m, realm);
            if (m == null) {
                throw new Exception("method not found");
            }
            Object ret = m.invoke(ctrl);

            String result = "";
            if (ret == null) {
                result = "null";
            } else if (!TypeChecker.isValueOrString(ret)) {
                result = jsonHelper.convertToJson(ret);
            } else {
                result = ret.toString();
            }
            new ResultFilter(exchange);
            if (mySqlDbContext != null)
                mySqlDbContext.commit();
            write200ForNonSet(exchange, result);

        } catch (ClassNotFoundException cnfe) {
            TerminateResponseWith500(exchange, cnfe.getMessage());
        } catch (InvocationTargetException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String message = e.getTargetException().getMessage() + sw;
            TerminateResponseWith500(exchange, message);
        } catch (SQLException se) {
            TerminateResponseWith500(exchange, se.toString());
            if (mySqlDbContext != null) {
                try {
                    mySqlDbContext.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            TerminateResponseWith500(exchange, e.toString());
        } finally {
            try {
                if (mySqlDbContext != null && !mySqlDbContext.conn.isClosed()) {
                    mySqlDbContext.conn.close();
                }
            } catch (SQLException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        }
    }

    private void checkPermission(Class ctrlClass, Method src, String realm) throws Exception {
        if (src != null) {
            Anonymous am = src.getClass() == Method.class ? ((Method) src).getDeclaredAnnotation(Anonymous.class) : ((Class<?>) ctrlClass).getDeclaredAnnotation(Anonymous.class);
            if (am != null) return;
            Permission permMethod = src.getDeclaredAnnotation(Permission.class);
            Permission permCtrl = (Permission) ctrlClass.getDeclaredAnnotation(Permission.class);
            if (permCtrl != null) {
                String pname1 = permCtrl.name();
                if (realm.indexOf("[" + pname1 + "]") == -1) {
                    throw new Exception("No Permission");
                }
            }

            if (permMethod != null) {
                String pname1 = permMethod.name();
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
            }
        }
        return null;
    }


    private void write200ForNonSet(HttpExchange exchange, String result) throws IOException {
        int code = exchange.getResponseCode();
        if (code == -1) {
            exchange.sendResponseHeaders(200, result.length());
        }
        exchange.getResponseBody().write(result.getBytes());
        exchange.getResponseBody().close();
        exchange.close();
    }

    public void TerminateResponseWith500(HttpExchange exchange, String message) throws IOException {
        logger.error(message);
        exchange.sendResponseHeaders(500, message.length());
        exchange.getResponseBody().write(message.getBytes());
        exchange.getResponseBody().close();
        exchange.close();

    }
}
