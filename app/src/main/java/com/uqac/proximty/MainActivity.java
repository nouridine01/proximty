package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.User;

import java.util.ArrayList;
import java.util.List;

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
        u.setPseudo("noor");
        u.setPassword("123");
        userDao.insertUsers(u);
        User u2 = new User();
        u2.setLastName("bili");
        u2.setFirstName("oumar");
        u2.setPseudo("bili");
        u.setPassword("123");
        userDao.insertUsers(u2);

        List<User> list=new ArrayList<>();
        try {
            System.out.println("----------------list users----------------");
            userDao.getAll().forEach(user->{
                System.out.println(user.getPseudo());
                //list.add(user);
            });
            User c = userDao.connexion("noor","123");
            System.out.println("----------------connection----------------");
            System.out.println(c.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}