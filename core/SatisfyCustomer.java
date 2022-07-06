package core;
import com.sun.net.httpserver.HttpExchange;
import utils.CustomerContext;
import utils.JsonHelper;
import utils.PropertyUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class SatisfyCustomer {
    protected HttpExchange exchange;
    protected CustomerContext customerContext;
    protected JsonHelper jsonHelper;
    protected PropertyUtil properties;

    public SatisfyCustomer(CustomerContext exchange, PropertyUtil properties) {
        super();
        this.customerContext =exchange;
        this.exchange = exchange.getHttpExchange();
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

    protected void OK(String content) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(content.getBytes());
        exchange.getResponseBody().close();
    }

    protected void json(Object src) throws InvocationTargetException, IllegalAccessException, IOException {
        String content=jsonHelper.convertToJson(src);
        exchange.getResponseHeaders().set("content-type", "application/json");
        OK(content);
    }

    protected String getProperty(String name){
        return properties.getValue(name);
    }

    protected String getAppInfo(){
        return getProperty("calo.app.name")+";"+getProperty("calo.app.version");
    }
}