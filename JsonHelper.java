import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
