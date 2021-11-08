package com.uqac.proximty.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/*@Entity(primaryKeys = {"user_id", "interest_id"},
        foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "uid",
                childColumns = "user_id"),
        @ForeignKey(entity = Interest.class,
                parentColumns = "id",
                childColumns = "interest_id")
})*/
public class UserInterestCrossRef {
    @ColumnInfo(name = "user_id")
    public long userId;
    @ColumnInfo(name = "interest_id")
    public long interestId;
}
