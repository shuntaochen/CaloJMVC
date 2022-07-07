package core;
import com.sun.net.httpserver.HttpExchange;
import utils.CustomerContext;
import utils.JsonHelper;
import utils.PropertyUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class Satisfact {
    protected HttpExchange exchange;
    protected CustomerContext customerContext;
    protected JsonHelper jsonHelper;
    protected PropertyUtil properties;

    public Satisfact(CustomerContext context, PropertyUtil properties) {
        super();
        this.customerContext =context;
        this.exchange = context.getHttpExchange();
        this.jsonHelper=new JsonHelper();
        this.properties=properties;
    }

    protected Map<String,String> query(){
        return customerContext.queryMap;
    }
    protected String query(String key){
        return customerContext.queryMap.get(key);
    }
    protected Map<String,String> requestBody(){
        return customerContext.requestBodyMap;
    }
    protected String request(String key){
        return query(key)==""||query(key)==null?requestBody().get(key):query(key);
    }

    protected Object getRequestHeader(Object key){
        return exchange.getRequestHeaders().get(key);
    }

    protected void jsonResponseHeader(){
        exchange.getResponseHeaders().set("content-type", "application/json");
    }

    protected String getProperty(String name){
        String ret= properties.getValue(name);
        return ret;
    }

    protected String getAppInfo(){
        return getProperty("calo.app.name")+";"+getProperty("calo.app.version");
    }
}