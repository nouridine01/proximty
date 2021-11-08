package com.uqac.proximty.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;

/*@Entity(primaryKeys = {"userId", "friendId"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "uid",
                        childColumns = "userId"),
                @ForeignKey(entity = User.class,
                        parentColumns = "uid",
                        childColumns = "friendId")
        })*/
public class UserFriendCrossRef {
    public long userId;
    public long friendId;
}
