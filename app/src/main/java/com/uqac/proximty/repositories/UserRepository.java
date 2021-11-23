package com.uqac.proximty.repositories;

import android.content.Context;

import com.uqac.proximty.PrefManager;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.User;

public class UserRepository {
    private UserDao userDao;
    AppDatabase appDatabase;

    public UserRepository(Context context) {
        this.userDao = AppDatabase.getDatabase(context).userDao();
    }

    public User getConnectedUser(long id){
        return userDao.getUserById(id);
    }
}
