package com.uqac.proximty.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class UserWithInterests {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "uid",
            entityColumn = "id",
            associateBy = @Junction(UserInterestCrossRef.class)
    )
    public List<Interest> interests;
}
