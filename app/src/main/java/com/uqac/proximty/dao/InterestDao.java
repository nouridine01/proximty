package com.uqac.proximty.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;

import java.util.List;

@Dao
public interface InterestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertInterests(Interest... interests);

    @Delete
    void delete(Interest interest);

    @Query("SELECT * FROM Interest i WHERE i.name = :name")
    Interest getInterestByName(String name);

    @Query("SELECT * FROM Interest")
    List<Interest> getAll();
}
