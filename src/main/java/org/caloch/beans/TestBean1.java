package org.caloch.beans;

public class TestBean1 extends Entity {

    public TestBean1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    String name;
}
