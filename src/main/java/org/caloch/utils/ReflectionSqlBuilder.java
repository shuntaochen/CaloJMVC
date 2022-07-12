package org.caloch.utils;

import org.caloch.core.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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


    public <TBean> HashMap<String, Map.Entry<String, Object>> getPresentFieldValuesWithoutId(TBean bean) throws IllegalAccessException {
        HashMap<String, Map.Entry<String, Object>> ret = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val = field.get(bean);
            field.setAccessible(flag);
            if (val != null && !isBasicDefaultValue(val)) {
                if (!fieldName.equals("id")) {
                    AbstractMap.SimpleEntry<String, Object> entry = new AbstractMap.SimpleEntry(field.getType(), val);
                    ret.put(fieldName, entry);
                }
            }
        }
        return ret;
    }

    public <TBean> HashMap<String, Object> getPresentFieldValuesMapWithoutId(TBean bean) throws IllegalAccessException {
        HashMap<String, Object> ret = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val = field.get(bean);
            field.setAccessible(flag);
            if (val != null && !isBasicDefaultValue(val)) {
                if (!fieldName.equals("id"))
                    ret.put(fieldName, val);
            }
        }
        return ret;
    }


    private String wrap(String src) {
        return "(" + src + ")";
    }

    private <TBean> ArrayList<String> buildFieldExpressionsWithoutId(TBean bean) throws
            IllegalAccessException {
        ArrayList<String> ret = new ArrayList<>();
        HashMap<String, Object> m = getPresentFieldValuesMapWithoutId(bean);
        for (Map.Entry<String, Object> e : m.entrySet()) {
            ret.add(e.getKey() + "=" + e.getValue());
        }
        return ret;
    }


    public <TBean extends Entity> String createSelectSql(TBean bean) throws IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        ArrayList<String> exps = buildFieldExpressionsWithoutId(bean);
        String sqlwhere = "";
        if (!exps.isEmpty()) {
            String conds = String.join(" and ", exps);
            sqlwhere = conds.equals("") ? "" : " and " + conds;
            if (bean.getId() != 0) {
                sqlwhere = " and id=" + bean.getId();
            }
        }
        return "select " + String.join(",", getPresentFieldValuesMapWithoutId(bean).keySet()) + " from " + tableName + " where 1=1 " + sqlwhere;
    }


    public <TBean extends Entity> String createUpdateSql(TBean bean) throws
            IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        String dest = String.join(",", buildFieldExpressionsWithoutId(bean));
        return "update " + tableName + " set " + dest + " where id=" + bean.getId();
    }

    public <TBean extends Entity> String createDeleteSql(TBean bean) throws IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        ArrayList<String> exps = buildFieldExpressionsWithoutId(bean);
        String sqlwhere = "";
        if (!exps.isEmpty()) {
            String conds = String.join(" and ", exps);
            sqlwhere = conds.equals("") ? "" : " and " + conds;
            if (bean.getId() != 0) {
                sqlwhere = " and id=" + bean.getId();
            }
        }
        return "delete from " + tableName + " where  1=1 " + sqlwhere;
    }


    public <TBean extends Entity> String createInsertSql(TBean bean) throws
            IllegalAccessException {
        HashMap<String, String> m = getPresentFieldsConditionMapWithoutId(bean);
        String dest = String.join(",", m.values());
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "insert into " + tableName + "(" + String.join(",", m.keySet()) + ") " + "values" + wrap(dest);
    }


    //region statements

    public <TBean> HashMap<String, String> getPresentFieldsConditionMapWithoutId(TBean bean) throws IllegalAccessException {
        HashMap<String, String> ret = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val = field.get(bean);
            field.setAccessible(flag);
            if (val != null && !isBasicDefaultValue(val) && !field.equals("id")) {
                if (!val.getClass().equals(Boolean.class) && !(val instanceof Number))
                    ret.put(fieldName, "'" + val + "'");
                else ret.put(fieldName, val.toString());
            }
        }
        return ret;
    }

    private <TBean> ArrayList<String> buildPresentFieldsTemplate(TBean bean) throws
            IllegalAccessException {
        ArrayList<String> ret = new ArrayList<>();
        HashMap<String, String> m = getPresentFieldsConditionMapWithoutId(bean);
        for (Map.Entry<String, String> e : m.entrySet()) {
            ret.add(e.getKey() + "=?");
        }
        return ret;
    }

    public <TBean extends Entity> String createSelectStatement(TBean bean) throws
            IllegalAccessException {
        ArrayList<String> parts = buildPresentFieldsTemplate(bean);
        String sqlwhere = "";
        if (!parts.isEmpty()) {
            String conds = String.join(" and ", parts);
            sqlwhere = conds.equals("") ? "" : " and " + conds;
        }
        return "select " + String.join(",", getPresentFieldValuesMapWithoutId(bean).keySet()) + " from " + bean.getClass().getSimpleName().toLowerCase() + " where 1=1 " + sqlwhere;
    }

    public <TBean extends Entity> String createUpdateStatement(TBean bean) throws
            IllegalAccessException {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        String dest = String.join(",", buildPresentFieldsTemplate(bean));
        return "update " + tableName + " set " + dest + " where id=?";
    }

    public <TBean extends Entity> String createDeleteStatement(TBean bean) {
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "delete from " + tableName + " where id= ?";
    }


    public <TBean extends Entity> String createInsertStatement(TBean bean) throws
            IllegalAccessException {
        HashMap<String, Object> m = getPresentFieldValuesMapWithoutId(bean);
        List<String> vs = new ArrayList<>();
        for (int i = 0; i < m.values().size(); i++) {
            vs.add("?");
        }

        String dest = String.join(",", vs);
        String tableName = bean.getClass().getSimpleName().toLowerCase();
        return "insert into " + tableName + "(" + String.join(",", m.keySet()) + ") " + "values" + wrap(dest);
    }

    //endregion

}
