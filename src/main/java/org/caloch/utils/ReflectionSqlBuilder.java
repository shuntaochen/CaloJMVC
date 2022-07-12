package org.caloch.utils;

import org.caloch.beans.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionSqlBuilder {

    public interface getReqParam {
        public String get(String name);
    }

    public static <TBean> TBean inflate(TBean bean, getReqParam op) throws InvocationTargetException, IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field m : fields) {
            for (Field field : fields) {
                String result = op.get(field.getName());
                if (result != null) {
                    boolean flag = field.canAccess(bean);
                    field.setAccessible(true);
                    String ft = field.getType().getSimpleName();
                    if ("int".equals(ft)) {
                        field.setInt(bean, Integer.parseInt(result));
                    } else {
                        field.set(bean, result);
                    }

                    field.setAccessible(flag);
                }
            }
        }
        return bean;
    }

    public static <TBean> TBean inflateNew(Class tp, getReqParam op) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Field[] fields = tp.getDeclaredFields();
        TBean instance = (TBean) tp.getDeclaredConstructor().newInstance();
        for (Field m : fields) {
            for (Field field : fields) {
                String result = op.get(field.getName());
                if (result != null) {
                    boolean flag = field.canAccess(instance);
                    field.setAccessible(true);
                    String ft = field.getType().getSimpleName();
                    if ("int".equals(ft)) {
                        field.setInt(instance, Integer.parseInt(result));
                    } else {
                        field.set(instance, result);
                    }
                    field.setAccessible(flag);
                }
            }
        }
        return instance;
    }


    private boolean isBasicDefaultValue(Object src) {
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
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val = field.get(bean);
            field.setAccessible(flag);
            if (val != null && !isBasicDefaultValue(val)) {
                if (!val.getClass().equals(Boolean.class) && !(val instanceof Number))
                    ret.put(fieldName, "'" + val + "'");
                else ret.put(fieldName, val.toString());
            }
        }
        return ret;
    }

    private String wrap(String src) {
        return "(" + src + ")";
    }

    private <TBean> ArrayList<String> buildPresentFields(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        ArrayList<String> ret = new ArrayList<>();
        HashMap<String, String> m = getPresentFieldsMap(bean);
        for (Map.Entry<String, String> e : m.entrySet()) {
            ret.add(e.getKey() + "=" + e.getValue());
        }
        return ret;
    }

    private <TBean> ArrayList<String> buildPresentFieldsTemplate(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        ArrayList<String> ret = new ArrayList<>();
        HashMap<String, String> m = getPresentFieldsMap(bean);
        for (Map.Entry<String, String> e : m.entrySet()) {
            ret.add(e.getKey() + "=?");
        }
        return ret;
    }

    public <TBean> String createSelectSql(TBean bean) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> parts = buildPresentFields(bean);
        String sqlwhere = "";
        if (!parts.isEmpty()) {
            String conds = String.join(" and ", parts);
            sqlwhere = conds.equals("") ? "" : " and " + conds;
        }
        return "select " + String.join(",", getPresentFieldsMap(bean).keySet()) + " from " + bean.getClass().getSimpleName().toLowerCase() + " where 1=1 " + sqlwhere;
    }

    public <TBean extends Entity> String createUpdateSql(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        HashMap<String, String> m = getPresentFieldsMap(bean);
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        String dest = String.join(",", buildPresentFields(bean));
        return "update " + tableName + " set " + dest + " where id=" + bean.getId();
    }

    public <TBean extends Entity> String createDeleteSql(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "delete from " + tableName + " where id= " + bean.getId();
    }

    public <TBean extends Entity> String createInsertSql(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        HashMap<String, String> m = getPresentFieldsMap(bean);
        String dest = String.join(",", m.values());
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "insert into " + tableName + "(" + String.join(",", m.keySet()) + ") " + "values" + wrap(dest);
    }

    public <TBean extends Entity> String createSelectStatement(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        ArrayList<String> parts = buildPresentFieldsTemplate(bean);
        String sqlwhere = "";
        if (!parts.isEmpty()) {
            String conds = String.join(" and ", parts);
            sqlwhere = conds.equals("") ? "" : " and " + conds;
        }
        return "select " + String.join(",", getPresentFieldsMap(bean).keySet()) + " from " + bean.getClass().getSimpleName().toLowerCase() + " where 1=1 " + sqlwhere;
    }

    public <TBean extends Entity> String createUpdateStatement(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        HashMap<String, String> m = getPresentFieldsMap(bean);
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        String dest = String.join(",", buildPresentFieldsTemplate(bean));
        return "update " + tableName + " set " + dest + " where id=?";
    }

    public <TBean extends Entity> String createDeleteStatement(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "delete from " + tableName + " where id= ?";
    }


    public <TBean extends Entity> String createInsertStatement(TBean bean) throws
            InvocationTargetException, IllegalAccessException {
        HashMap<String, String> m = getPresentFieldsMap(bean);
        List<String> vs = new ArrayList<>();
        for (int i = 0; i < m.values().size(); i++) {
            vs.add("?");
        }
        String dest = String.join(",", vs);
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "insert into " + tableName + "(" + String.join(",", m.keySet()) + ") " + "values" + wrap(dest);
    }

}
