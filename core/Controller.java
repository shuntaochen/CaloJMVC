package core;
import com.sun.net.httpserver.HttpExchange;
import utils.JsonHelper;
import utils.PropertyUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class Controller {
    protected HttpExchange exchange;
    protected JsonHelper jsonHelper;
    protected PropertyUtil properties;

    public Controller(HttpExchange exchange,PropertyUtil properties) {
        super();
        this.exchange = exchange;
        this.jsonHelper=new JsonHelper();
        this.properties=properties;
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