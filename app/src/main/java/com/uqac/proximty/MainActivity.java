package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.uqac.proximty.broadcasts.WiFiDirectBroadcastReceiver;
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


public class MainActivity extends AppCompatActivity implements WifiP2pManager.ChannelListener, Scan_page.DeviceActionListener {
    //@Inject
    AppDatabase appDatabase;
    UserDao userDao;
    InterestDao interestDao;
    UserInterestCrossRefDao userInterestCrossRefDao;
    UserFriendCrossRefDao userFriendCrossRefDao;
    TextView text;

    ArrayList<Contact> contacts;

    public static final String TAG = "wifiscan";

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;

    private WifiP2pManager manager;
    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;

    private PrefManager prefManager;
    private UserRepository userRepository;

    private TabAdapter adapter;

    public TabAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TabAdapter adapter) {
        this.adapter = adapter;
    }

    public WifiP2pManager getManager() {
        return manager;
    }

    public void setManager(WifiP2pManager manager) {
        this.manager = manager;
    }

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }

    public void setChannel(WifiP2pManager.Channel channel) {
        this.channel = channel;
    }

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
                R.drawable.ic_baseline_message_24,
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




        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        if (!initP2p()) {
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
            // After this point you wait for callback in
            // onRequestPermissionsResult(int, String[], int[]) overridden method
        }


        //initialisation();

    }

    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Fine location permission is not granted!");
                    finish();
                }
                break;
        }
    }

    private boolean initP2p() {
        // Device capability definition check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)) {
            Log.e(TAG, "Wi-Fi Direct is not supported by this device.");
            return false;
        }

        // Hardware capability check
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            Log.e(TAG, "Cannot get Wi-Fi system service.");
            return false;
        }

        if (!wifiManager.isP2pSupported()) {
            Log.e(TAG, "Wi-Fi Direct is not supported by the hardware or Wi-Fi is off.");
            return false;
        }

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        if (manager == null) {
            Log.e(TAG, "Cannot get Wi-Fi Direct system service.");
            return false;
        }

        channel = manager.initialize(this, getMainLooper(), null);
        if (channel == null) {
            Log.e(TAG, "Cannot initialize Wi-Fi Direct.");
            return false;
        }

        return true;
    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() {
        Scan_page scan_page=(Scan_page) adapter.getItem(0);

        if (scan_page.getPeers() != null) {
            scan_page.clearPeers();
        }

    }


    private void launchWalktroughScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, WalkthroughActivity.class));
    }


    public void lunchAct(View view) {
        startActivity(new Intent(this, WalkthroughActivity.class));
    }

    //interface Scan_page.DeviceActionListener
    @Override
    public void showDetails(WifiP2pDevice device) {
        /*DeviceDetailFragment fragment = (DeviceDetailFragment) getFragmentManager()
                .findFragmentById(R.id.frag_detail);
        fragment.showDetails(device);*/

    }

    @Override
    public void connect(WifiP2pConfig config) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {

        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);

            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "Disconnect success");
            }

        });
    }


    @Override
    public void cancelDisconnect() {

        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (manager != null) {


            manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Aborting connection",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reasonCode) {
                    Toast.makeText(MainActivity.this,
                            "Connect abort request failed. Reason Code: " + reasonCode,
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override //channel interface
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            resetData();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
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
        //

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
}