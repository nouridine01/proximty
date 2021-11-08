package com.uqac.proximty.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithFriends {
    /*@Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "friendId",
            associateBy = @Junction(UserFriendCrossRef.class)
    )
    public List<User> friends;*/
}
