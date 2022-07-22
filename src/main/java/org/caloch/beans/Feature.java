package org.caloch.beans;

import org.caloch.core.Column;
import org.caloch.core.Entity;

public class Feature extends Entity {
    @Column(length = 40)
    public String name;
    public int code;
    public boolean isDisabled;
}
