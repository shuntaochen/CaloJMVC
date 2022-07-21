package org.caloch.utils;

import java.util.*;

public class TypeChecker {

    public void Check() {
        int[] x0 = new int[]{2, 3, 4};
        List<Object> xxx = convertArrayToList(x0);
        List z1 = Arrays.asList(x0);
        Object x11 = z1.get(0);

        Collection y = Arrays.asList(x0);
        List<Integer> l0 = new ArrayList<>();
        int[] a0 = new int[]{2, 3, 4};
        l0.add(2);
        Object x = 2;
        Object x1 = 2.3;
        Object x2 = "test";
        Object x3 = true;

        Object a = x instanceof Number;//true
        Object b = x1 instanceof Number;//true
        Object c = x2 instanceof String;//true
        Object d = x3 instanceof Boolean;//true
        assert x1 instanceof Number;
        //other types, instance of sth else
        assert !x3.getClass().isArray();
        assert Collection.class.isAssignableFrom(l0.getClass());
        Object e = a0.getClass().isArray();//true
        assert a0.getClass().isArray();


    }


    public static boolean isCollection(Object src) {
        boolean ret = Collection.class.isAssignableFrom((src.getClass()));
        return ret;
    }

    public static boolean isArray(Object src) {
        return src.getClass().isArray();
    }

    public static List<Object> convertArrayToList(Object src) {
        List<Object> ret = new ArrayList<Object>();
        if (src.getClass().isArray()) {
            List x = Arrays.asList(src);
            Object y = x.get(0);

            String typeName = y.getClass().getComponentType().getSimpleName();//"int",
            //int,String,long,double,float,boolean,byte,char,short
            boolean canAssign = Object.class.isAssignableFrom(y.getClass().getComponentType().getClass());//int, false
            switch (typeName) {
                case "int":
                    int[] z = (int[]) src;
                    for (int z0 : z) {
                        ret.add(z0);
                    }
                    break;
                case "String":
                    String[] z1 = (String[]) src;
                    for (String z0 : z1) {
                        ret.add(z0);
                    }
                    break;
                case "long":
                    long[] z2 = (long[]) src;
                    for (long z0 : z2) {
                        ret.add(z0);
                    }
                    break;
                case "double":
                    double[] z3 = (double[]) src;
                    for (double z0 : z3) {
                        ret.add(z0);
                    }
                    break;
                case "float":
                    float[] z4 = (float[]) src;
                    for (float z0 : z4) {
                        ret.add(z0);
                    }
                    break;
                case "boolean":
                    boolean[] z5 = (boolean[]) src;
                    for (boolean z0 : z5) {
                        ret.add(z0);
                    }
                    break;
                case "short":
                    short[] z6 = (short[]) src;
                    for (short z0 : z6) {
                        ret.add(z0);
                    }
                    break;
                case "byte":
                    byte[] z7 = (byte[]) src;
                    for (byte z0 : z7) {
                        ret.add(z0);
                    }
                    break;
                case "char":
                    char[] z8 = (char[]) src;
                    for (char z0 : z8) {
                        ret.add(z0);
                    }
                    break;

                default:
                    Object[] z9 = (Object[]) src;
                    for (Object z0 : z9) {
                        ret.add(z0);
                    }

            }
        }
        return ret;
    }

    public static <T> List<T> ArrayToListConversion(T array[]) {
        List<T> list = new ArrayList<>();
        for (T t : array) {
            list.add(t);
        }
        return list;
    }


    public static boolean isMap(Object src) {
        return Map.class.isAssignableFrom(src.getClass());
    }

    public static boolean isValueOrString(Object src) {
        boolean ret;
        ret = Number.class.isAssignableFrom(src.getClass()) || src.getClass() == String.class || src.getClass() == Boolean.class;
        return ret;
    }

    public static boolean isValueObject(Object src) {
        boolean ret;
        ret = Number.class.isAssignableFrom(src.getClass()) || src.getClass() == Boolean.class;
        return ret;
    }

    public static boolean isBasicDefaultValue(Object src) {
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
        if (clazz.equals(Boolean.class)) {
            return (boolean) src == false;
        }

        return false;
    }
}
