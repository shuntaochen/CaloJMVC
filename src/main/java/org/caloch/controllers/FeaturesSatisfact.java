package org.caloch.controllers;

import org.caloch.beans.Feature;
import org.caloch.core.BaseSatisfact;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.sql.SQLException;

public class FeaturesSatisfact extends BaseSatisfact<Feature> {
    public FeaturesSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
        setBeanName(Feature.class.getSimpleName());
    }


    public int getMaxCode() throws SQLException {
        int code = (int) mysqlDbContext.max("code", new Feature());
        return code;
    }
}
