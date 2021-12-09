package com.uqac.proximty.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserFriendCrossRef;
import com.uqac.proximty.entities.UserInterestCrossRef;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//@Database(entities = {User.class, Interest.class,UserInterestCrossRef.class, UserFriendCrossRef.class}, version = 1)
public abstract class AppDatabase /*extends RoomDatabase*/ {
    /*public abstract UserDao userDao();

    public abstract InterestDao interestDao();
    public abstract UserFriendCrossRefDao userFriendCrossRefDao();
    public abstract UserInterestCrossRefDao userInterestCrossRefDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /*public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "word_database").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }*/

}