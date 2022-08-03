package org.caloch.utils;

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

public class CustomerContext {

    public HttpExchange getHttpExchange() {
        return _exchange;
    }

    private HttpExchange _exchange;
    public Map<String, String> queryMap;
    public Map<String, String> requestBodyMap;
    protected OutputStream response;

    public String requestBodyString;

    protected void StatusCode(int code) throws IOException {
        _exchange.sendResponseHeaders(code, 0);
    }

    public CustomerContext(HttpExchange exchange) throws IOException {
        super();
        _exchange = exchange;
        String query = exchange.getRequestURI().getQuery();
        queryMap = formDataToMap(query);
        String postString = readStreamToString(exchange.getRequestBody());
        requestBodyString=postString;
        requestBodyMap = formDataToMap(postString);
        response = exchange.getResponseBody();
    }

    public String readStreamToString(InputStream input) throws IOException {
        StringBuilder ret = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        while (br.read() != -1) {
            ret.append(br.readLine());
        }
        return ret.toString();
    }


    public Map<String, String> formDataToMap(String formData) {
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
                    result.put(key.toLowerCase(), val);
                } catch (UnsupportedEncodingException e) {
                }
            }
        });
        return result;
    }

}