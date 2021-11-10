package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.InterestDao;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.dao.UserFriendCrossRefDao;
import com.uqac.proximty.dao.UserInterestCrossRefDao;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserFriendCrossRef;
import com.uqac.proximty.entities.UserInterestCrossRef;
import com.uqac.proximty.entities.UserWithFriends;
import com.uqac.proximty.entities.UserWithInterests;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


//@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    //@Inject
    AppDatabase appDatabase;
    UserDao userDao;
    InterestDao interestDao;
    UserInterestCrossRefDao userInterestCrossRefDao;
    UserFriendCrossRefDao userFriendCrossRefDao;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialisation
        text=findViewById(R.id.text);
        userDao=AppDatabase.getDatabase(this).userDao();
        interestDao=AppDatabase.getDatabase(this).interestDao();
        userFriendCrossRefDao=AppDatabase.getDatabase(this).userFriendCrossRefDao();
        userInterestCrossRefDao=AppDatabase.getDatabase(this).userInterestCrossRefDao();
                // insertion users
        User u = new User();
        u.setLastName("nouridine");
        u.setFirstName("oumarou");
        u.setPseudo("noor");
        u.setPassword("123");
        //userDao.insertUsers(u);
        User u2 = new User();
        u2.setLastName("bili");
        u2.setFirstName("oumar");
        u2.setPseudo("bili");
        u.setPassword("123");
        //userDao.insertUsers(u2);

        //insertion interest
        Interest t1 = new Interest();
        t1.setName("Lecture");
        Interest t2 = new Interest();
        t2.setName("Soccer");

        //interestDao.insertInterests(t1,t2);

        //creation friend
        UserFriendCrossRef userFriendCrossRef = new UserFriendCrossRef();
        userFriendCrossRef.setUid(userDao.getUserByPseudo("noor").getUid());
        userFriendCrossRef.setFriend(userDao.getUserByPseudo("bili").getUid());
        //userFriendCrossRefDao.insertUserFriendCrossRef(userFriendCrossRef);

        //attribute interest
        UserInterestCrossRef userInterestCrossRef=new UserInterestCrossRef();
        userInterestCrossRef.setUid(userDao.getUserByPseudo("noor").getUid());
        userInterestCrossRef.setId(interestDao.getInterestByName("Lecture").getId());
        //userInterestCrossRefDao.insertUserInterestCrossRef(userInterestCrossRef);



        List<User> list=new ArrayList<>();
        try {
            System.out.println("----------------list users----------------");
            userDao.getAll().forEach(user->{
                System.out.println(user.toString());

            });

            System.out.println("----------------list interests----------------");
            interestDao.getAll().forEach(in->{
                System.out.println(in.toString());

            });

            System.out.println("----------------list user friend cross ref----------------");
            userFriendCrossRefDao.getAll().forEach(i->{
                System.out.println(i.toString());

            });

            User c = userDao.connexion("noor","123");
            System.out.println("----------------connection----------------");
            System.out.println(c.toString());

            System.out.println("----------------user friends----------------");
            UserWithFriends userWithFriends = userDao.getUserWithFriends(userDao.getUserByPseudo("noor").getUid());
            System.out.println(userWithFriends.friends.get(0).toString());

            System.out.println("----------------user interests----------------");
            UserWithInterests userWithInterests = userDao.getUserWithInterests(userDao.getUserByPseudo("noor").getUid());
            System.out.println(userWithInterests.interests.get(0).getName());




        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}