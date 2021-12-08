package com.uqac.proximty.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class UserWithFriends {
    /*@Embedded
    public User user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "uid",
            associateBy = @Junction(UserFriendCrossRef.class)
    )*/
    public List<User> friends = new ArrayList<>();


}
