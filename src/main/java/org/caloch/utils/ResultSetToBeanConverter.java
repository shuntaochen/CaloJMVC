package org.caloch.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ResultSetToBeanConverter {
    private ResultSetToBeanConverter() {
    }

    public static <T> ArrayList<T> getBeans(ResultSet resultSet, Class clazz) {
        ArrayList<T> list = new ArrayList();
        Field fields[] = clazz.getFields();
        try {
            while (resultSet.next()) {
                T instance = (T) clazz.getDeclaredConstructor().newInstance();
                for (Field field : fields) {
                    String colName = field.getName();
                    try {
                        resultSet.findColumn(colName);
                    } catch (SQLException e) {
                        System.out.println("Resultset does not have column" + colName + " for type:" + clazz.getTypeName());
                        continue;
                    }
                    Object result = resultSet.getObject(colName);
                    boolean flag = field.canAccess(instance);
                    field.setAccessible(true);
                    try {
                        field.set(instance, result);
                    } catch (Exception ex) {
                    }
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

    public static <T> T getBean(ResultSet resultSet, Class clazz) {
        T instance = null;
        try {
            instance = (T) clazz.getDeclaredConstructor().newInstance();
            Field fields[] = clazz.getFields();
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