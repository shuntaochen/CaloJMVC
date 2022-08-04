package org.caloch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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


    @Test
    public void test1() throws IOException {
        String json = "{ \"deleteFlag\":23,\"codeId\":[\"1\",2,4],\"codeName\":\"IT\",\"sort\":\"1\",\"id\":\"60\"}";
        StringReader sr = new StringReader(json);
        ArrayList<String> words = new ArrayList<>();
        String curW = "";
        while (true) {
            int cur = sr.read();
            if (cur == -1) break;
            if ((char) cur == ',') {
                words.add(",");
            }
            if ((char) cur == '{') {
                words.add("{");
            } else if ((char) cur == '}') {
                words.add("}");
            } else if ((char) cur == ':') {
                words.add(":");
            } else if ((char) cur == '[') {
                words.add("[");
            } else if ((char) cur == ']') {
                words.add("]");
            } else if (cur != ' ' && cur != '\"' && cur != ',') {
                String w = "" + (char) cur;
                while (true) {
                    int cur1 = sr.read();
                    if (cur1 == -1) break;
                    if (cur1 == ',' || cur1 == '}' || cur1 == ']') {
                        words.add(w);
                        words.add("" + (char) cur1);
                        break;
                    }
                    w += (char) cur1;
                }
            } else if (cur == '\"') {
                String w = "";
                while (true) {
                    int cur1 = sr.read();
                    if (cur1 == -1) break;
                    if (cur1 != '\"') {
                        w += (char) cur1;
                    } else if (cur1 == '\"') {
                        words.add(w);
                        break;
                    }
                }
            } else if ((char) cur == ' ') {
                continue;
            }

        }
        for (String w : words) {
            System.out.println(w);
        }
    }
}
