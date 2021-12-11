package com.uqac.proximty.activities;
/**
 * https://www.geeksforgeeks.org/viewpager-using-fragments-in-android-with-example/
 * https://www.androidhive.info/2016/05/android-build-intro-slider-app/
 * https://appsnipp.com/free-illustrated-onboarding-intro-slider-designs/
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uqac.proximty.MainActivity;
import com.uqac.proximty.R;
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
import com.uqac.proximty.repositories.InterestRepository;

import java.util.ArrayList;
import java.util.List;

public class WalkthroughActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private TextView btnSkip;
    AppDatabase appDatabase;
    UserDao userDao;
    InterestDao interestDao;
    UserInterestCrossRefDao userInterestCrossRefDao;
    UserFriendCrossRefDao userFriendCrossRefDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        viewPager =  findViewById(R.id.view_pager);
        dotsLayout =  findViewById(R.id.layoutDots);
        btnSkip =  findViewById(R.id.btn_skip);


        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        addBottomDots(0);

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        initialisation();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(5, 0, 0, 0);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setWidth(30);
            dots[i].setHeight(5);
            dots[i].setLayoutParams(textLayoutParams);
            dots[i].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.onboarding_dotunselected, null));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }

        //hiding the last dot , as the last layout is dummy for handling exit on last page
        dots[layouts.length-1].setVisibility(View.GONE);

        if (dots.length > 0){
            dots[currentPage].setWidth(70);
            dots[currentPage].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.onboarding_dot_selected, null));
        }
    }

    private void initialisation(){
        //initialisation

        //userDao=AppDatabase.getDatabase(this).userDao();
        //interestDao=AppDatabase.getDatabase(this).interestDao();
        //userFriendCrossRefDao=AppDatabase.getDatabase(this).userFriendCrossRefDao();
        //userInterestCrossRefDao=AppDatabase.getDatabase(this).userInterestCrossRefDao();
        // insertion users
        /*User u = new User();
        u.setLastName("nouridine");
        u.setFirstName("oumarou");
        u.setPseudo("noor");
        u.setPassword("123");

        User u2 = new User();
        u2.setLastName("bili");
        u2.setFirstName("oumar");
        u2.setPseudo("bili");
        u.setPassword("123");
        userDao.insertUsers(u,u2);*/

        InterestRepository ir = new InterestRepository();

        //insertion interest
        try {
            Interest t1 = new Interest(); t1.setName("Lecture"); ir.add(t1);
            Interest t2 = new Interest(); t2.setName("Sport"); ir.add(t2);
            Interest t3 = new Interest(); t3.setName("Jeu Vidéo"); ir.add(t3);
            Interest t4 = new Interest(); t4.setName("Voyage"); ir.add(t4);
            Interest t5 = new Interest(); t5.setName("Cinéma & Séries"); ir.add(t5);
            Interest t6 = new Interest(); t6.setName("Cuisine"); ir.add(t6);
            Interest t7 = new Interest(); t7.setName("Sciences"); ir.add(t7);
            Interest t8 = new Interest(); t8.setName("Politique"); ir.add(t8);
            Interest t9 = new Interest(); t9.setName("Mode"); ir.add(t9);
            Interest t10 = new Interest(); t10.setName("Soirées"); ir.add(t10);
            Interest t11 = new Interest(); t11.setName("Photographie"); ir.add(t11);
            Interest t12 = new Interest(); t12.setName("Danse"); ir.add(t12);
            Interest t13 = new Interest(); t13.setName("Théâtre"); ir.add(t13);
            Interest t14 = new Interest(); t14.setName("Arts"); ir.add(t14);
            Interest t15 = new Interest(); t15.setName("Bien-être & santé"); ir.add(t15);
            System.out.println("Interests successfully added");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //interestDao.insertInterests(t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15);

        /*//creation friend
        UserFriendCrossRef userFriendCrossRef = new UserFriendCrossRef();
        userFriendCrossRef.setUid(userDao.getUserByPseudo("noor").getUid());
        userFriendCrossRef.setFriend(userDao.getUserByPseudo("bili").getUid());
        //userFriendCrossRefDao.insertUserFriendCrossRef(userFriendCrossRef);*/

        //attribute interest
        /*UserInterestCrossRef userInterestCrossRef=new UserInterestCrossRef();
        userInterestCrossRef.setUid(userDao.getUserByPseudo("noor").getUid());
        userInterestCrossRef.setId(interestDao.getInterestByName("Lecture").getId());
        userInterestCrossRefDao.insertUserInterestCrossRef(userInterestCrossRef);*/


        /*List<User> list=new ArrayList<>();
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
            //UserWithFriends userWithFriends = userDao.getUserWithFriends(userDao.getUserByPseudo("noor").getUid());
            //System.out.println(userWithFriends.friends.get(0).toString());

            System.out.println("----------------user interests----------------");
            UserWithInterests userWithInterests = userDao.getUserWithInterests(userDao.getUserByPseudo("noor").getUid());
            System.out.println(userWithInterests.interests.get(0).getName());




        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/
    }

    private void launchHomeScreen() {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, LoginActivity.class));
        //finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
//                btnNext.setText(getString(R.string.start));
                btnSkip.setText("SKIP INTRO");
                launchHomeScreen();
//                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
//                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
