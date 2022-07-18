package org.caloch.controllers;

import org.caloch.beans.News;
import org.caloch.core.Satisfact;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.util.ArrayList;
import java.util.List;

public class FrontendSatisfact extends Satisfact {
    public FrontendSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }


    public List<News> getNews() {
        return new ArrayList<>();
    }
}
