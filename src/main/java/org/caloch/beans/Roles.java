package org.caloch.beans;

import org.caloch.core.Column;
import org.caloch.core.Entity;
import org.caloch.core.Table;

@Table(name = "Roles")
public class Roles extends Entity {
    @Column(length = 20)
    public String name;
}
