package com.uqac.proximty.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.uqac.proximty.entities.UserFriendCrossRef;


@Dao
public interface UserFriendCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserFriendCrossRef(UserFriendCrossRef... UserFriendCrossRefs);
}
