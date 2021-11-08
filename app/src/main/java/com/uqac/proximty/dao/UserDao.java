package com.uqac.proximty.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserWithFriends;
import com.uqac.proximty.entities.UserWithInterests;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(User... users);

    //@Insert
    //public void insertUsersAndFriends(User user, List<User> friends);

    @Insert
    public void insertUsersAndInterests(User user, List<Interest> interests);

    @Update
    public void updateUsers(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User")
    List<User> getAll();

    /*@Transaction
    @Query("SELECT * FROM User")
    public List<UserWithFriends> getUserWithFriends();*/

    /*@Transaction
    @Query("SELECT * FROM User")
    public List<UserWithInterests> getUserWithInterests();*/
}
