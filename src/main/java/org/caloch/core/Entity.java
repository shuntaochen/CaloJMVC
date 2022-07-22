package org.caloch.core;

public class Entity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String zjm;

    public int id;

    public int insertedBy;
    public long insertedOn;
    public int updatedBy;
    public long updatedOn;
    public int deletedBy;
    public long deletedOn;
    public boolean isDeleted;
    public long expireOn;
}
