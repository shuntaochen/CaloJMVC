package org.caloch.beans;

import org.caloch.core.Column;
import org.caloch.core.Entity;
import org.caloch.core.Table;

public class User extends Entity {
    @Column(length = 20)
    public String name;

    public String password;

    @Column(length = 60)
    public String email;
}
