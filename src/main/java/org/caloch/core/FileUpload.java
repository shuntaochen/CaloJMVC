package org.caloch.core;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class FileUpload extends FormDataHandle {
    @Override
    public void handle(HttpExchange httpExchange, List<MultiPart> parts) {//first file is always empty, less than actual amount,
        //first fileupload can be empty, it is ok,
    }
}
