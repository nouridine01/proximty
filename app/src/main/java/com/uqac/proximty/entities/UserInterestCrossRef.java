package com.uqac.proximty.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"uid", "id"})
public class UserInterestCrossRef {

    public long uid;

    public long id;
}
