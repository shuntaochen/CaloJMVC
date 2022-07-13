package org.caloch.beans;

import org.caloch.core.Column;
import org.caloch.core.Entity;
import org.caloch.core.Table;

@Table(name = "Roles")
public class Roles extends Entity {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Name")
    String name;
}
