package com.uqac.proximty.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserFriendCrossRef;

import java.util.List;


@Dao
public interface UserFriendCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserFriendCrossRef(UserFriendCrossRef... UserFriendCrossRefs);
    @Query("SELECT * FROM UserFriendCrossRef")
    List<UserFriendCrossRef> getAll();
}
