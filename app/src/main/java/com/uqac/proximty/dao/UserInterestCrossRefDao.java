package com.uqac.proximty.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserInterestCrossRef;

@Dao
public interface UserInterestCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserInterestCrossRef(UserInterestCrossRef... UserInterestCrossRefs);

    @Delete
    void delete(Interest interest);
}
