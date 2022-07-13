package org.caloch.utils;

import org.caloch.core.Column;
import org.caloch.core.Entity;
import org.caloch.core.Table;

import java.lang.reflect.Field;
import java.util.*;

public class BeanDbParser<T extends Entity> {


    T bean;

    public BeanDbParser(T bean) {
        this.bean = bean;
        this.beanInfo = getPresentFieldsInfoWithoutId(bean);
        this.tableName = mapTable();
        if (bean.getId() != 0)
            id = bean.getId();
    }


    private String mapTable() {
        Table table = bean.getClass().getDeclaredAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }
        return bean.getClass().getSimpleName();
    }

    private String mapColumn(Field field) {
        Column col = field.getDeclaredAnnotation(Column.class);
        if (col != null) {
            return col.name();
        }
        return field.getName();
    }

    public <TBean> HashMap<String, Map.Entry<String, Object>> getPresentFieldsInfoWithoutId(TBean bean) {
        HashMap<String, Map.Entry<String, Object>> ret = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = mapColumn(field);
            boolean flag = field.canAccess(bean);
            field.setAccessible(true);
            Object val;
            try {
                val = field.get(bean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            field.setAccessible(flag);
            String type = field.getType().getSimpleName();
            if (val != null && !TypeChecker.isBasicDefaultValue(val)) {
                if (!fieldName.equalsIgnoreCase("id")) {
                    AbstractMap.SimpleEntry<String, Object> entry = new AbstractMap.SimpleEntry(type, val);
                    ret.put(fieldName, entry);
                }
            }
        }
        return ret;
    }


    public HashMap<String, Map.Entry<String, Object>> beanInfo;
    int id;
    String tableName;
    String fieldNamesString;
    String insertValuesString;
    String insertValuesTemplate;
    String equalsStringComma;
    String equalsStringCommaTemplate;

    String equalsStringAnd;

    String equalsStringAndTemplate;
    String whereString;
    String whereStringTemplate;

    private String wrapSq(String src) {
        return "'" + src + "'";
    }

    private String wrapParethesis(String src) {
        return "(" + src + ")";
    }

    public void parse() {
        this.fieldNamesString = String.join(",", this.beanInfo.keySet());
        ArrayList<String> r0 = new ArrayList<>();
        ArrayList<String> r00 = new ArrayList<>();
        LoopFieldsInfo((fieldName, fieldInfo) -> {
            Object val = fieldInfo.getValue();
            r0.add(fieldName + "=" + (TypeChecker.isBasicDefaultValue(val) ? val.toString() : wrapSq(val.toString())));
            r00.add(fieldName + "=" + "?");
        });
        this.equalsStringComma = String.join(",", r0);
        this.equalsStringCommaTemplate = String.join(",", r00);//a=2,b=3
        this.equalsStringAnd = String.join(" and ", r0);//a=2 and b=3,
        this.equalsStringAndTemplate = String.join(" and ", r00);//a=? and b=?
        this.whereString = equalsStringAnd.equals("") ? "" : (" where " + equalsStringAnd);// ""/ where a=2 and b=3
        this.whereStringTemplate = equalsStringAndTemplate.equals("") ? "" : (" where " + equalsStringAndTemplate);// ""/ where a=? and b=?
        ArrayList<String> r1 = new ArrayList<>();
        ArrayList<String> r10 = new ArrayList<>();
        LoopFieldsInfo((fieldName, fieldInfo) -> {
            Object val = fieldInfo.getValue();
            r1.add(TypeChecker.isBasicDefaultValue(val) ? val.toString() : wrapSq(val.toString()));
            r10.add("?");
        });
        this.insertValuesString = wrapParethesis(String.join(",", r1));//(2,'a',3)
        this.insertValuesTemplate = wrapParethesis(String.join(",", r10));//(?,?,?)
    }

    public String buildSelectSqlTemplate() {
        if (id != 0) return "select * from " + tableName + "  WHERE Id=?";//select * from tb where id=?
        return "select * from " + tableName + whereStringTemplate;//select * from tb where a=? and b=?
    }

    public String buildUpdateSqlTemplate() {
        if (fieldNamesString.equals("")) throw new IllegalArgumentException("Bean has no fields to update");
        String updateFields = wrapParethesis(fieldNamesString + ",Id");//a,b,c,id
        String setEqualStrings = equalsStringComma.equals("") ? "" : (" set " + equalsStringCommaTemplate);//""/set a=?, b=?
        return "update " + tableName + setEqualStrings + " where Id=?";//update tb(a,b,c) set (a=?,b=?) where id=?
    }

    public String buildInsertSqlTemplate() {
        if (fieldNamesString.equals("")) throw new IllegalArgumentException("Insert columns not specified.");
        return "insert into " + tableName + wrapParethesis(fieldNamesString) + " values " + insertValuesTemplate;//insert into tb(a,b,c) values (?,?,?)
    }

    public String buildDeleteSqlTemplate() {
        if (id != 0) return "delete from " + tableName + " where Id=?";//delete from tb where id=?
        return "delete from " + tableName + whereStringTemplate;//delete from tb where a=? and b=?
    }


    interface ProcessMapEntry {
        void handle(String fieldName, Map.Entry<String, Object> fieldInfo);
    }

    private void LoopFieldsInfo(ProcessMapEntry handler) {
        for (Map.Entry<String, Map.Entry<String, Object>> line : beanInfo.entrySet()) {
            handler.handle(line.getKey(), line.getValue());
        }
    }

}
