package org.caloch.beans;

import org.caloch.core.Column;
import org.caloch.core.Entity;
import org.caloch.core.JsonIgnore;

public class Feature extends Entity {
    @Column(length = 40)
    public String name;
    public int code;
    public boolean isEnabled;

    @JsonIgnore
    public String description;
}
