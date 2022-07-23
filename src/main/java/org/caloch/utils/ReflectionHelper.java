package org.caloch.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionHelper {


    public interface getReqParam {
        public String get(String name);
    }

    public static <TBean> TBean inflate(TBean bean, getReqParam op) throws IllegalAccessException {
        Field[] fields = bean.getClass().getFields();
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
        Field[] fields = tp.getFields();
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

    public static <TDest, TSrc> TDest map(TSrc src, Class dest) {
        try {
            TDest instance = (TDest) dest.getDeclaredConstructor().newInstance();
            Field[] fields = src.getClass().getFields();
            Field[] fieldsDest = dest.getFields();

            for (Field field : fieldsDest) {
                Object val = getValFromSrc(src, field.getName(), fields);
                if (val != null) {
                    boolean flag = field.canAccess(instance);
                    field.setAccessible(true);
                    field.set(instance, val);
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

    private static <TSrc> Object getValFromSrc(TSrc src, String name, Field[] fields) throws IllegalAccessException {
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(name)) {
                boolean flag = field.canAccess(src);
                field.setAccessible(true);
                Object val = field.get(src);
                field.setAccessible(flag);
                return val;
            }
        }
        return null;
    }


}
