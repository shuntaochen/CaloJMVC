//wait to implement my Mvc by paring URI
//Controller can be abstract
import com.sun.net.httpserver.HttpExchange;

public class Controller {
    protected HttpExchange exchange;

    public Controller(HttpExchange exchange) {
        super();
        this.exchange = exchange;
    }

}