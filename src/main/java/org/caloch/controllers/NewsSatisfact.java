package org.caloch.controllers;

import org.caloch.beans.Feature;
import org.caloch.beans.News;
import org.caloch.core.BaseSatisfact;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.sql.SQLException;

public class NewsSatisfact extends BaseSatisfact<Feature> {
    public NewsSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
        setBeanName(News.class.getSimpleName());
    }


}
