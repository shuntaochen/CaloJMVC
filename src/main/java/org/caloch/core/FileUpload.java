package org.caloch.core;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class FileUpload extends FormDataHandle {
    @Override
    public void handle(HttpExchange httpExchange, List<MultiPart> parts) throws IOException {//only support 1 file and form, this code, skip it for now,
        for (MultiPart part : parts) {//put filename geenration to front, only supports last file
            String filename = part.filename;
            Date d = new Date();
            String projPath = System.getProperty("user.dir");
            String path = projPath + File.separator + "webapp" + File.separator + "upload" + File.separator + filename;
            File f = new File(path);
            if (!f.exists())
                f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(part.bytes);
            fos.flush();
            fos.close();
        }

    }
}
