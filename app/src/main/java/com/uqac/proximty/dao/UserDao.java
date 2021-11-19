package com.uqac.proximty.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.RoomWarnings;
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

    @Insert
    public void insertUsersAndFriends(User user, List<User> friends);

    @Insert
    public void insertUsersAndInterests(User user, List<Interest> interests);

    @Update
    public void updateUsers(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User")
    List<User> getAll();

    @RewriteQueriesToDropUnusedColumns
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT u.* FROM User u JOIN UserFriendCrossRef uf ON u.uid=uf.friend WHERE uf.uid=:id AND u.uid<>:id")
    List<User> getUserFriends(Long id);


    @Query("SELECT * FROM User u WHERE u.pseudo = :pseudo AND u.password = :pwd")
    User connexion(String pseudo, String pwd);

    @Query("SELECT * FROM User u WHERE u.pseudo = :pseudo")
    User getUserByPseudo(String pseudo);

    @Query("SELECT * FROM User u WHERE u.uid = :id")
    User getUserById(long id);

    @Transaction
    @Query("SELECT * FROM User")
    public List<UserWithFriends> getUserWithFriends();

    /*@Transaction
    @RewriteQueriesToDropUnusedColumns
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    //@Query("SELECT * FROM User u JOIN UserFriendCrossRef uf ON u.uid=uf.uid JOIN User f ON f.uid=uf.friend WHERE u.uid=:id")
    @Query("SELECT * FROM User u WHERE u.uid=:id")
    public UserWithFriends getUserWithFriends(long id);*/

    @Transaction
    @Query("SELECT * FROM User u WHERE u.uid=:id")
    public UserWithInterests getUserWithInterests(long id);



    @Transaction
    @Query("SELECT * FROM User")
    public List<UserWithInterests> getUserWithInterests();
}
