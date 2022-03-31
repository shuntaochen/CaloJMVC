import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class TestController extends Controller {

    public TestController(HttpExchange exchange) {
        super(exchange);
        // TODO Auto-generated constructor stub
    }

    public void aa() throws IOException {
        System.out.println("Hello controller");
        exchange.sendResponseHeaders(200, 0);//header must be sent first before writing response content;
        exchange.getResponseBody().write("Hello me.".getBytes());
        exchange.getResponseBody().close();

    }

}
