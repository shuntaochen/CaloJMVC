package org.caloch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class SimpleJsonTest {


    @Test
    public void test() throws IOException {
        String json = "{\"deleteFlag\":0,\"codeId\":\"1\",\"codeName\":\"IT\",\"sort\":\"1\",\"id\":\"60\"}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            MstCode mstCode = mapper.readValue(json, MstCode.class);
            System.out.print(mstCode.getCodeName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json1 = "[{\"deleteFlag\":0,\"codeId\":\"1\",\"codeName\":\"IT\",\"sort\":\"1\",\"id\":\"60\"},{\"deleteFlag\":0,\"codeId\":\"2\",\"codeName\":\"Accounting\",\"sort\":\"2\",\"id\":\"61\"},{\"deleteFlag\":0,\"codeId\":\"3\",\"codeName\":\"Support\",\"sort\":\"3\",\"id\":\"62\"}]";
        ObjectMapper mapper1 = new ObjectMapper();
        try {
            List<MstCode> mstCodes = mapper1.readValue(json1, mapper.getTypeFactory().constructCollectionType(List.class, MstCode.class));
            System.out.println(mstCodes.size());
            System.out.println(mstCodes.get(0).getCodeName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
