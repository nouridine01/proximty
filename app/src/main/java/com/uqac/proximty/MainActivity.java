package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.uqac.proximty.activities.WalkthroughActivity;
import com.uqac.proximty.adaptaters.ContactsAdapter;
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

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            launchWalktroughScreen();
            finish();
        }

        //setContentView(R.layout.activity_users);
        setContentView(R.layout.scan);


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