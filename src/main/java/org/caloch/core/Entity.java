package org.caloch.core;

public class Entity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;

    public int insertedBy;
    public String insertedOn;
    public int updatedBy;
    public String updatedOn;
    public int deletedBy;
    public String deletedOn;
    public boolean isDeleted;
}
