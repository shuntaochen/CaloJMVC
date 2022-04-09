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
                else if (TypeChecker.isCollection(value)||TypeChecker.isArray(value)){
                    String mid="";
                    List looper=new ArrayList();
                    if(TypeChecker.isCollection(value))
                        looper=(List)value;
                    if(TypeChecker.isArray(value))
                        looper=TypeChecker.convertArrayToList(value);
                    for(Object member:(List)looper){
                        mid+= (!TypeChecker.isValueObject(member) ? convertToJson(member):member)+",";
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
