package utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class JsonHelper {


    public JsonHelper() {

    }

    public String convertToJson(Object src) throws InvocationTargetException, IllegalAccessException {
        StringBuffer ret = new StringBuffer("{");
        Method[] methods = src.getClass().getDeclaredMethods();
        for (Method m : methods) {
            String mn = m.getName();
            if (mn.startsWith("get") && Character.isUpperCase(mn.charAt(3))) {
                String key = mn.substring(3);
                Object value = m.invoke(src);
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
                } else if (value.getClass() == String.class) {
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
