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

    User user;

    private PrefManager prefManager;

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        user = (User) data.getParcelable("user");
        System.out.println(user.toString());

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

    }

    private void launchWalktroughScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, WalkthroughActivity.class));
    }


    public void lunchAct(View view) {
        startActivity(new Intent(this, WalkthroughActivity.class));
    }
}