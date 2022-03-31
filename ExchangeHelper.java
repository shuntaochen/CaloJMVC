import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExchangeHelper {

    public HttpExchange _exchange;
    protected Map<String, String> queryString;
    protected Map<String, String> body;
    protected OutputStream response;

    protected void StatusCode(int code) throws IOException {
        _exchange.sendResponseHeaders(code, 0);
    }

    public ExchangeHelper(HttpExchange exchange) throws IOException {
        super();
        _exchange = exchange;
        String query = exchange.getRequestURI().getQuery();
        queryString = formData2Dic(query);
        String postString = readStreamToString(exchange.getRequestBody());
        body = formData2Dic(postString);
        response = exchange.getResponseBody();
    }

    public static String readStreamToString(InputStream input) throws IOException {
        StringBuilder ret = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        while (br.read() != -1) {
            ret.append(br.readLine());
        }
        return ret.toString();
    }

    public static Map<String, String> formData2Dic(String formData) {
        Map<String, String> result = new HashMap<>();
        if (formData == null || formData.trim().length() == 0) {
            return result;
        }
        final String[] items = formData.split("&");
        Arrays.stream(items).forEach(item -> {
            final String[] keyAndVal = item.split("=");
            if (keyAndVal.length == 2) {
                try {
                    final String key = URLDecoder.decode(keyAndVal[0], "utf8");
                    final String val = URLDecoder.decode(keyAndVal[1], "utf8");
                    result.put(key, val);
                } catch (UnsupportedEncodingException e) {
                }
            }
        });
        return result;
    }

}