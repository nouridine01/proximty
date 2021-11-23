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
                        parentColumns = {"uid"},
                        childColumns = {"friend"})
        },
        indices = {@Index(value = {"friend"})})
public class UserFriendCrossRef {
    private long uid;
    private long friend;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getFriend() {
        return friend;
    }

    public void setFriend(long friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "UserFriendCrossRef{" +
                "uid=" + uid +
                ", friend=" + friend +
                '}';
    }
}
