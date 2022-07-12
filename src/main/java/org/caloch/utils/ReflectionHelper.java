package org.caloch.utils;

import org.caloch.beans.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReflectionHelper {


    private boolean isBaseDefaultValue(Object src) {//boolean is not included,
        Class<?> clazz = src.getClass();
        if (clazz.equals(Byte.class)) {
            return (byte) src == 0;
        }
        if (clazz.equals(Integer.class)) {
            return (int) src == 0;
        }
        if (clazz.equals(Short.class)) {
            return (short) src == 0;
        }
        if (clazz.equals(Long.class)) {
            return (long) src == 0;
        }
        if (clazz.equals(Character.class)) {
            return (char) src == '\u0000';
        }
        if (clazz.equals(Float.class)) {
            return (float) src == 0.0;
        }
        if (clazz.equals(Double.class)) {
            return (double) src == 0.0;
        }
//        if (clazz.equals(Boolean.class)) {
//            return (boolean) src == false;
//        }

        return false;
    }


    public <TBean> HashMap<String, String> getPresentFieldsMap(TBean bean) throws InvocationTargetException, IllegalAccessException {
        HashMap<String, String> ret = new HashMap<>();
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                String name = m.getName().substring(3).toLowerCase();
                Object val = m.invoke(bean);
                if (val != null && !isBaseDefaultValue(val)) {
                    ret.put(name, "'" + val + "'");
                }
            }
        }
        return ret;
    }

    private String wrap(String src) {
        return "(" + src + ")";
    }

    public <TBean> ArrayList<String> buildPresentFields(TBean bean) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> ret = new ArrayList<>();
        HashMap<String, String> m = getPresentFieldsMap(bean);
        for (Map.Entry<String, String> e : m.entrySet()) {
            ret.add(e.getKey() + "=" + e.getValue());
        }
        return ret;
    }

    public <TBean> String createSelectSql(TBean bean) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> parts = buildPresentFields(bean);
        String dest = "";
        if (!parts.isEmpty()) {
            String sql = String.join(" and ", parts);
            dest = sql.equals("") ? "" : " and " + sql;
        }
        return "select " + String.join(",", getPresentFieldsMap(bean).keySet()) + " from " + bean.getClass().getSimpleName().toLowerCase() + " where 1=1 " + dest;
    }

    public <TBean extends Entity> String createUpdateSql(TBean bean) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> parts = buildPresentFields(bean);
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        String dest = "";
        if (!parts.isEmpty()) {
            dest = String.join(" and ", parts);
        }
        return "update " + tableName + "(" + String.join(",", getPresentFieldsMap(bean).keySet()) + ") " + " set" + dest + " where id= " + bean.getId();
    }

    public <TBean extends Entity> String createDeleteSql(TBean bean) throws InvocationTargetException, IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "delete from " + tableName + " where id= " + bean.getId();
    }

    public <TBean extends Entity> String createInsertSql(TBean bean) throws InvocationTargetException, IllegalAccessException {
        HashMap<String, String> m = getPresentFieldsMap(bean);
        String dest = String.join(",", m.values());
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "insert into " + tableName + "(" + String.join(",", m.keySet()) + ") " + "values" + wrap(dest);
    }


}
