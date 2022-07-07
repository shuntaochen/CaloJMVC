package beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public  class TestBean{
    private String name;
    private int age;

    public TestBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int[] getAr(){
        return new int[]{2,3,4};
    }

    private HashMap<String,Integer> BMap;

    public List<TestBean1> getLi(){
        List<TestBean1> ret= new ArrayList<TestBean1>(){
        };
        ret.add(new TestBean1("a"));
        ret.add(new TestBean1("b"));
        return ret;
    }


    public HashMap<String, Integer> getBMap() {
        return BMap;
    }

    public void setBMap(HashMap<String, Integer> BMap) {
        this.BMap = BMap;
    }
}