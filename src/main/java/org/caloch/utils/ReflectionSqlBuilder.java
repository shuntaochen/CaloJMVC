package org.caloch.utils;

import org.caloch.core.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReflectionSqlBuilder {


    public interface getReqParam {
        public String get(String name);
    }

    public static <TBean> TBean inflate(TBean bean, getReqParam op) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field m : fields) {
            for (Field field : fields) {
                String result = op.get(field.getName().toLowerCase());
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

    public static <TBean> TBean inflateNew(Class tp, getReqParam op) {
        Field[] fields = tp.getDeclaredFields();
        TBean instance = null;
        try {
            instance = (TBean) tp.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                String result = op.get(field.getName().toLowerCase());
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
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public <TBean> HashMap<String, Object> getKeyValueMapWithoutId(TBean bean) throws IllegalAccessException {
        HashMap<String, Object> ret = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val = field.get(bean);
            field.setAccessible(flag);
            if (val != null && !TypeChecker.isBasicDefaultValue(val)) {
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
        HashMap<String, Object> m = getKeyValueMapWithoutId(bean);
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
        return "select " + String.join(",", getKeyValueMapWithoutId(bean).keySet()) + " from " + tableName + " where 1=1 " + sqlwhere;
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

    public <TBean> HashMap<String, String> getPresentFieldsConditionMapWithoutId(TBean bean) throws IllegalAccessException {
        HashMap<String, String> ret = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val = field.get(bean);
            field.setAccessible(flag);
            if (val != null && !TypeChecker.isBasicDefaultValue(val) && !field.equals("id")) {
                if (!val.getClass().equals(Boolean.class) && !(val instanceof Number))
                    ret.put(fieldName, "'" + val + "'");
                else ret.put(fieldName, val.toString());
            }
        }
        return ret;
    }


}
