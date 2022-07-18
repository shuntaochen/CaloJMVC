package org.caloch.core;

public class Entity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;

    public String insertedBy;
    public String insertedOn;
    public String updatedBy;
    public String updatedOn;
    public String deletedBy;
    public String deletedOn;
    public boolean isDeleted;
}
