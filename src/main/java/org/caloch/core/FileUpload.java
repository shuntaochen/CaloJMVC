package org.caloch.core;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUpload extends FormDataHandle {
    @Override
    public void handle(HttpExchange httpExchange, List<MultiPart> parts) throws IOException {//only support 1 file and form, this code, skip it for now,
        ArrayList<String> list = new ArrayList<>();
        for (MultiPart part : parts) {//put filename geenration to front, only supports last file
            if(part.bytes==null)continue;
            Date d = new Date();
            String filename = "[" + d.getTime() + "]" + part.filename;
            String projPath = System.getProperty("user.dir");
            String path = projPath + File.separator + "upload" + File.separator + filename;
            File f = new File(path);
            if (!f.exists())
                f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(part.bytes);
            fos.flush();
            fos.close();
            list.add(filename);
        }
        String ret = String.join("|", list);
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
//        httpExchange.getResponseHeaders().set("Content-Type", "text/html");
        httpExchange.sendResponseHeaders(200, ret.length());
        httpExchange.getResponseBody().write(ret.getBytes(StandardCharsets.UTF_8));
        httpExchange.getRequestBody().close();
        httpExchange.close();
    }
}
