package utils;

public class TypeChecker {

    public void Check(){
        Object x=2;
        Object x1=2.3;
        Object x2="test";
        Object x3=true;

        Object a=x instanceof Number;//true
        Object b=x1 instanceof Number;//true
        Object c=x2 instanceof String;//true
        Object d= x3 instanceof Boolean;//true
        assert x1 instanceof Number;

    }
}
