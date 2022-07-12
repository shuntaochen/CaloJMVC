package org.caloch.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetBeanFromResultUtil {
    private GetBeanFromResultUtil() {
    }

    public static <T> List getBeans(ResultSet resultSet, T src) {
        List list = new ArrayList();
        Class className = src.getClass();
        Field fields[] = className.getDeclaredFields();
        try {
            while (resultSet.next()) {
                for (Field field : fields) {
                    Object result = resultSet.getObject(field.getName());
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    field.set(src, result);
                    field.setAccessible(flag);
                }
                list.add(src);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T getBean(ResultSet resultSet, T src) {
        try {
            Field fields[] = src.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object result = resultSet.getObject(field.getName());
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                field.set(src, result);
                field.setAccessible(flag);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return src;
    }

}
