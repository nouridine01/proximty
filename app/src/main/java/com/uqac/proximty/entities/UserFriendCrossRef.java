package com.uqac.proximty.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(primaryKeys = {"uid", "friend"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "uid",
                        childColumns = "uid"),
                @ForeignKey(entity = User.class,
                        parentColumns = "uid",
                        childColumns = "friend")
        },
        indices = {@Index(value = {"friend"})})
public class UserFriendCrossRef {
    public long uid;
    public long friend;
}
