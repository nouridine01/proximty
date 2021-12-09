package com.uqac.proximty.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

//@Entity(primaryKeys = {"uid", "id"})
public class UserInterestCrossRef {

    private long uid;

    private long id;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
