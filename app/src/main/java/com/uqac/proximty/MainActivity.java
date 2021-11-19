package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.uqac.proximty.fragments.NotificationFragment;
import com.uqac.proximty.fragments.ProfilFragment;
import com.uqac.proximty.fragments.Scan_page;
import com.uqac.proximty.activities.WalkthroughActivity;
import com.uqac.proximty.adaptaters.ContactsAdapter;
import com.uqac.proximty.adaptaters.TabAdapter;
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
import com.uqac.proximty.fragments.Contact_page;
import com.uqac.proximty.models.Contact;
import com.uqac.proximty.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


//@AndroidEntryPoint


public class MainActivity extends AppCompatActivity {
    //@Inject
    AppDatabase appDatabase;
    UserDao userDao;
    InterestDao interestDao;
    UserInterestCrossRefDao userInterestCrossRefDao;
    UserFriendCrossRefDao userFriendCrossRefDao;
    TextView text;

    ArrayList<Contact> contacts;

    private PrefManager prefManager;
    private UserRepository userRepository;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository(this);
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            launchWalktroughScreen();
            finish();
        }

        setContentView(R.layout.activity_main);


        int[] tabIcons = {
                R.drawable.scan,
                R.drawable.email,
                R.drawable.notification,
                R.drawable.user
        };

        viewPager =  findViewById(R.id.viewPager);
        tabLayout =  findViewById(R.id.tabLayout);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Scan_page(), "");
        adapter.addFragment(new Contact_page(), "");
        adapter.addFragment(new NotificationFragment(), "");
        adapter.addFragment(new ProfilFragment(), "");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

        /*// Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        contacts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!*/
        initialisation();

    }

    private void initialisation(){
        //initialisation

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

        User u2 = new User();
        u2.setLastName("bili");
        u2.setFirstName("oumar");
        u2.setPseudo("bili");
        u.setPassword("123");
        //userDao.insertUsers(u,u2);

        //insertion interest
        Interest t1 = new Interest();
        t1.setName("Lecture");
        Interest t2 = new Interest();
        t2.setName("Soccer");
        //interestDao.insertInterests(t1,t2);

        //creation friend
        UserFriendCrossRef userFriendCrossRef = new UserFriendCrossRef();
        userFriendCrossRef.setUid(userDao.getUserByPseudo("noor").getUid());
        userFriendCrossRef.setFriend(userDao.getUserByPseudo("test").getUid());
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

            System.out.println("----------------user interests----------------");
            UserWithInterests userWithInterests = userDao.getUserWithInterests(userDao.getUserByPseudo("noor").getUid());
            System.out.println(userWithInterests.interests.get(0).getName());

            System.out.println("----------------user friends----------------");
            //UserWithFriends userWithFriends = userDao.getUserWithFriends(userDao.getUserByPseudo("noor").getUid());
            List<User> users = userDao.getUserFriends(userDao.getUserByPseudo("noor").getUid());
            users.forEach(i->{
                System.out.println(i.toString());

            });

            //test connectedUser
            prefManager.setUserId(1);
            System.out.println("----------------connected user----------------");
            User user= userRepository.getConnectedUser(prefManager.getUserId());
            System.out.println(user.toString());


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void launchWalktroughScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, WalkthroughActivity.class));
    }


    public void lunchAct(View view) {
        startActivity(new Intent(this, WalkthroughActivity.class));
    }
}