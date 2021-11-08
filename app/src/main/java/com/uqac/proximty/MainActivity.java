package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.User;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


//@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    //@Inject
    AppDatabase appDatabase;
    UserDao userDao;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.text);
        userDao=AppDatabase.getDatabase(this).userDao();
        User u = new User();
        u.setLastName("nouridine");
        u.setFirstName("oumarou");
        u.setPseudo("noor179");
        userDao.insertUsers(u);

        userDao.getAll().forEach(user->{
            user.setPseudo("nie18");
            userDao.updateUsers(user);
            text.setText(user.toString());
        });
    }
}