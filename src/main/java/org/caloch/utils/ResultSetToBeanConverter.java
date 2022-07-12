package org.caloch.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ResultSetToBeanConverter {
    private ResultSetToBeanConverter() {
    }

    public static <T> List getBeans(ResultSet resultSet, Class className) {
        List list = new ArrayList();
        Field fields[] = className.getDeclaredFields();
        try {
            while (resultSet.next()) {
                T instance = (T) className.getDeclaredConstructor().newInstance();
                for (Field field : fields) {
                    Object result = resultSet.getObject(field.getName());
                    boolean flag = field.canAccess(instance);
                    field.setAccessible(true);
                    field.set(instance, result);
                    field.setAccessible(flag);
                }
                list.add(instance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static <T> T getBean(ResultSet resultSet, Class className) {
        T instance = null;
        try {
            instance = (T) className.getDeclaredConstructor().newInstance();
            Field fields[] = className.getDeclaredFields();
            for (Field field : fields) {
                Object result = resultSet.getObject(field.getName());
                boolean flag = field.canAccess(instance);
                field.setAccessible(true);
                field.set(instance, result);
                field.setAccessible(flag);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

}