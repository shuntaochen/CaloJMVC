package org.caloch.utils;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class StrJsonReaderTest {

    private String dest = "{\"bb\":5,\"ab\":\"a34b\\\\\\\"bsdf\"}";
    StringReader stringReader = new StringReader(dest);
    int counter = 0;
    //position: type:key, content: val,

    @Test
    public void readerJonsTest() throws IOException {
        Object a=readBoundaryChar();
        Object a1=readKey();
        Object a2=readBoundaryChar();
        Object a3=readVal();
        readBoundaryChar();

    }

//readJarry, if val type, read final, if object type, readObject, readtoken, read while !stream.end, return jarray, readsplitter,
// readObject, 还是要递归， 然后readval,readstring,readarray,readjarray, 这里要read jobject ,直到jarray 结束 ，主要就是readObject,应该就会结束了，先不写了，

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

    private Object readVal() throws IOException {

        String ret = "";
        while (true) {
            char cur = (char) stringReader.read();
            boolean hit=cur==',';
            if (cur != '}' && cur != ':' && cur != ',' && cur != ']') {
                ret += cur;
            } else {
                return ret;
            }
        }
    }

    private String readStringFromString() throws IOException {
        String ret = "";
        int count = 0;
        while (true) {
            counter++;
            char cur = (char) stringReader.read();
            if (cur == '\"') {
                count++;
                if (count == 2) {//结束条件才应该返回，判断好结束条件
                    return ret;
                }
                continue;
            }
            if (cur == '\\') {//needs escape, next char should append as plain char, 转义的要点就在于下一个字符要不要加入结果，这个其实是一个\
                char next = (char) stringReader.read();
                counter++;
                if (next == '\\') {
                    ret += next; //ret+=\
                } else
                    ret = ret + cur + next;//不是特殊的，直接合并
            }
            ret += cur;
        }

    }
}
