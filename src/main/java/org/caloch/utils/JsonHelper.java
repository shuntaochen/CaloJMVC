package org.caloch.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;


public class JsonHelper {


    public JsonHelper() {

    }

    public String convertToJson(Object src) throws InvocationTargetException, IllegalAccessException {
        if (TypeChecker.isCollection(src) || TypeChecker.isArray(src)) {
            String ret = "";
            String mid = "";
            List looper = new ArrayList();
            if (TypeChecker.isCollection(src))
                looper = (List) src;
            if (TypeChecker.isArray(src))
                looper = TypeChecker.convertArrayToList(src);
            for (Object member : (List) looper) {
                mid += (TypeChecker.isValueOrString(member) ? member : convertToJson(member)) + ",";
            }
            ret = "[" + mid.substring(0, mid.length() - 1) + "]";
            return ret;
        }
        else if(TypeChecker.isMap(src)){
            String mid = "";
            String value="";
            HashMap<Object,Object> looper=(HashMap<Object,Object>) src;
            for (Object k : looper.keySet()) {
                if(!TypeChecker.isValueOrString(k)) throw new UnsupportedOperationException("Object as Hashmap key serialization is not supported in json.");
                Object v=looper.get(k);
                mid += (!TypeChecker.isValueOrString(looper.get(k)) ? convertToJson(looper.get(k)) : ("\""+k+"\""+":"+ (v.getClass()==String.class?("\""+ looper.get(k)+"\""):looper.get(k)))+  ",");
            }
            value = "{" + mid.substring(0, mid.length() - 1) + "}";
            return value;
        }
        StringBuffer ret = new StringBuffer("{");
        Method[] methods = src.getClass().getDeclaredMethods();
        for (Method m : methods) {//for object type
            String mn = m.getName();
            if (mn.startsWith("get") && Character.isUpperCase(mn.charAt(3))) {
                String key = mn.substring(3);
                Object value = m.invoke(src);
                if(value==null){
                    value="null";
                }
                else
                if (TypeChecker.isValueObject(value)) {
                    //boolean or number, value keeps original value,
                } else if (TypeChecker.isCollection(value) || TypeChecker.isArray(value)) {
                    //array or collection
                    String mid = "";
                    List looper = new ArrayList();
                    if (TypeChecker.isCollection(value))
                        looper = (List) value;
                    if (TypeChecker.isArray(value))
                        looper = TypeChecker.convertArrayToList(value);
                    for (Object member : (List) looper) {//boxing numbers to Object
                        mid += (!TypeChecker.isValueOrString(member) ? convertToJson(member) : member) + ",";//member is object? json:
                        //hashmap, same with class
                    }
                    value = "[" + mid.substring(0, mid.length() - 1) + "]";//concat object json or to value type single array,
                }

                else if (value.getClass() == String.class) {
                    //string needs quotation symbol "key":"value"
                    value = "\"" + value + "\"";
                } else {
                    //class type, traverse the properties
                    value = convertToJson(value);
                }
                ret.append("\"" + key + "\":" + value + ",");//"key":value
            }
        }
        return ret.toString().substring(0, ret.length() - 1) + "}"; //finishing up one object
    }
}
