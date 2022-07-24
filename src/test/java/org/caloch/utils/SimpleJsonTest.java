package org.caloch.utils;

import org.json.simple.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class SimpleJsonTest {

    @Test
    public void test() throws IOException {
        JSONObject json = new JSONObject();
        json.put("a",22);
        StringWriter sw=new StringWriter();
        json.writeJSONString(sw);
        String jsonS= sw.toString();
    }
}
