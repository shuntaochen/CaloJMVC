package org.caloch.controllers;

import java.io.IOException;
import java.util.HashMap;

import com.mchange.v2.beans.swing.TestBean;
import org.caloch.utils.*;
import org.caloch.core.*;

public class OpenSatisfact extends Satisfact {


    public OpenSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
        //todo: add db helper, maybe mysql,
    }
    public HashMap<String, Integer> ff() {
        HashMap<String, Integer> ret = new HashMap<>();
        ret.put("name", 22);
        ret.put("age", 23);
        boolean b = TypeChecker.isMap(ret);
        Class<?> t = ret.getClass();
        return ret;
    }

    public void gg() throws IOException {
        redirect("https://baidu.com");
    }


}
