package core;
import com.sun.net.httpserver.HttpExchange;
import utils.ExchangeHelper;
import utils.JsonHelper;
import utils.PropertyUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class Controller {
    protected HttpExchange exchange;
    protected ExchangeHelper exchangeHelper;
    protected JsonHelper jsonHelper;
    protected PropertyUtil properties;

    public Controller(ExchangeHelper exchange, PropertyUtil properties) {
        super();
        this.exchangeHelper =exchange;
        this.exchange = exchange._exchange;
        this.jsonHelper=new JsonHelper();
        this.properties=properties;
    }

    protected Map<String,String> query(){
        return exchangeHelper.queryMap;
    }
    protected String query(String key){
        return exchangeHelper.queryMap.get(key);
    }
    protected Map<String,String> requestBody(){
        return exchangeHelper.requestBodyMap;
    }
    protected String request(String key){
        return query(key)==""||query(key)==null?requestBody().get(key):query(key);
    }

    protected Object getRequestHeader(Object key){
        return exchange.getRequestHeaders().get(key);
    }

    protected void OK(String content) throws IOException {
        exchange.sendResponseHeaders(200, 0);//header must be sent first before writing response content;
        exchange.getResponseBody().write(content.getBytes());
        exchange.getResponseBody().close();
    }

    protected void Json(Object src) throws InvocationTargetException, IllegalAccessException, IOException {
        String content=jsonHelper.convertToJson(src);
        OK(content);
    }

    protected String getProperty(String name){
        return properties.getValue(name);
    }

    protected String getAppInfo(){
        return getProperty("calo.app.name")+";"+getProperty("calo.app.version");
    }
}