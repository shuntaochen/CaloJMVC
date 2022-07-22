package org.caloch.controllers;

import org.caloch.beans.Feature;
import org.caloch.core.Satisfact;
import org.caloch.utils.CustomerContext;
import org.caloch.utils.JwtUtil;
import org.caloch.utils.PropertyUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class FeaturesSatisfact extends Satisfact {
    public FeaturesSatisfact(CustomerContext context, PropertyUtil properties, JwtUtil jwtUtil) {
        super(context, properties, jwtUtil);
    }

    /*
    @获取系统可控功能
     */
    public ArrayList<Feature> getAll() throws SQLException {
        ArrayList<Feature> features = mysqlDbContext.select(new Feature());
        return features;
    }

    public int getMaxCode() throws SQLException {
        int code=(int)mysqlDbContext.executeScalar("select max(code) from feature");
        return code;
    }
}
