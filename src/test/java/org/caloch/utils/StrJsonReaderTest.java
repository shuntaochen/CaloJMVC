package org.caloch.utils;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class StrJsonReaderTest {

    private String dest = "{\"ab\":\"ab\\\\\\\"bsdf\"}";
    StringReader stringReader = new StringReader(dest);
    int counter = 0;

    @Test
    public void readerJonsTest() throws IOException {
        int position = -1;
        readBoundaryChar();
        String s = readKey();
        char b = readBoundaryChar();
        String s1 = readStringFromString();
        char b1 = readBoundaryChar();

    }


    private char readBoundaryChar() throws IOException {
        while (true) {
            char cur = (char) stringReader.read();
            counter++;
            if (cur != ' ') {
                return cur;
            }
        }
    }

    private String readKey() throws IOException {
        String ret = "";
        int quoteCount = 0;
        boolean beginReadingStr = false;
        while (true) {
            counter++;
            char cur = (char) stringReader.read();
            if (cur == '\"') {//只有等于\"才操作
                quoteCount++;
                if (quoteCount == 1) {
                    beginReadingStr = true;
                }
            } else if (beginReadingStr) {//只有不等于\"才加
                ret += cur;
            }
            if (quoteCount == 2) {//记录到2说明匹配到两个引号，就返回
                return ret;
            }

        }
    }

    private String readStringFromString() throws IOException {

        String ret = "";
        boolean shouldReadStr = false;
        int pair = -1;
        while (true) {
            counter++;
            char cur = (char) stringReader.read();
            if (shouldReadStr) ret = ret + cur;
            if (cur == '\\') {
                cur = (char) stringReader.read();
                ret = ret + cur;
                continue;
            }
            if (cur == '\"') {
                pair++;
                shouldReadStr = true;
                System.out.println(cur);
                if (pair == 2) {
                    shouldReadStr = false;
                    return ret;
                }
            }
        }

    }
}
