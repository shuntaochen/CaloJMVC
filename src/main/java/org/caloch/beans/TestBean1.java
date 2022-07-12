package org.caloch.beans;

import org.caloch.core.Entity;

public class TestBean1 extends Entity {

    public TestBean1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    String name;
}
