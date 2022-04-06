package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeChecker {

    public void Check(){
        List<Integer> l0=new ArrayList<>();
        int[] a0=new int[]{2,3,4};
        l0.add(2);
        Object x=2;
        Object x1=2.3;
        Object x2="test";
        Object x3=true;

        Object a=x instanceof Number;//true
        Object b=x1 instanceof Number;//true
        Object c=x2 instanceof String;//true
        Object d= x3 instanceof Boolean;//true
        assert x1 instanceof Number;
        //other types, instance of sth else
        assert !x3.getClass().isArray();
        assert Collection.class.isAssignableFrom(l0.getClass());
        Object e=a0.getClass().isArray();//true
        assert a0.getClass().isArray();



    }
}
