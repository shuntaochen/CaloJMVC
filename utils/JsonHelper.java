package utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JsonHelper {



    public  JsonHelper(){

    }

    public String convertToJson(Object src) throws InvocationTargetException, IllegalAccessException {
        StringBuffer ret=new StringBuffer("{");
        Method[] methods= src.getClass().getDeclaredMethods();
        for (Method m:methods) {
            String mn=m.getName();
            if(mn.startsWith("get") && Character.isUpperCase(mn.charAt(3))){
                String key=mn.substring(3);
                Object value=m.invoke(src);
                if(value.getClass()==Integer.class){
                    //or use switch case instead,
                }
                else if (List.class.isAssignableFrom(value.getClass())){
                    String mid="";
                    for(Object member:(List)value){
                        mid+= (value instanceof Object? convertToJson(value):value)+",";
                    }
                    value="["+mid.substring(0,mid.length()-1)+"]";
                }
                else if(value.getClass()==String.class){
                    value="\""+value+"\"";
                }
                else{
                    value= convertToJson(value);
                }
                ret.append("\""+key+"\":"+value+",");
            }
        }
        return ret.toString().substring(0,ret.length()-1)+"}";
    }
}
