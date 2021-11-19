package com.uqac.proximty.fragments;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.skyfishjy.library.RippleBackground;
import com.uqac.proximty.R;

import java.util.ArrayList;

public class Scan_page extends Fragment implements View.OnClickListener {

    private static final String TAG = "Scan";
    private static final int SEPRATION_DIST_THRESHOLD = 50;

    private static int device_count = 0;

    RippleBackground rippleBackground;
    ImageView centerDeviceIcon;
    ArrayList<Point> device_points = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        return inflater.inflate(R.layout.scan, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate  (savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialSetup(view);
    }

    private void initialSetup(View view) {
        // layout files
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        centerDeviceIcon = (ImageView) view.findViewById(R.id.centerImage);
        // add onClick Listeners
        centerDeviceIcon.setOnClickListener(this);

        // center button position
        Display display = getActivity().getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        device_points.add(new Point(size.x / 2, size.y / 2));
        Log.d("MainActivity", size.x + "  " + size.y);

    }

    void checkLocationEnabled(){
        LocationManager lm = (LocationManager) Scan_page.this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(Scan_page.this.getActivity())
                    .setTitle(R.string.gps_network_not_enabled_title)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Scan_page.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel,null)
                    .show();
        }
    }

    @Override
    public void onClick(View view) {
        rippleBackground.startRippleAnimation();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                View tmp_device = createNewDevice("device" + device_count);
                rippleBackground.addView(tmp_device);
                foundDevice(tmp_device);
            }
        }, 5000);

    }

    public View createNewDevice(String device_name){
        View device1 = LayoutInflater.from(this.getActivity()).inflate(R.layout.device_icon, null);
        Point new_point = generateRandomPosition();
        RippleBackground.LayoutParams params = new RippleBackground.LayoutParams(350,350);
        params.setMargins(new_point.x, new_point.y, 0, 0);
        device1.setLayoutParams(params);

        TextView txt_device1 = device1.findViewById(R.id.myImageViewText);
        int device_id = (int)System.currentTimeMillis() + device_count++;
        txt_device1.setText(device_name);
        device1.setId(device_id);
        device1.setOnClickListener(this);

        device1.setVisibility(View.INVISIBLE);
        return device1;
    }

    private void foundDevice(View foundDevice){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

    Point generateRandomPosition(){
        Display display = getActivity().getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int SCREEN_WIDTH = size.x;
        int SCREEN_HEIGHT = size.y;

        int height_start = SCREEN_HEIGHT / 2 - 300;
        int x = 0;
        int y = 0;

        do{
            x = (int)(Math.random() * SCREEN_WIDTH);
            y = (int)(Math.random() * height_start);
        }while(checkPositionOverlap(new Point(x, y)));

        Point new_point = new Point(x, y);
        device_points.add(new_point);

        return new_point;

    }

    boolean checkPositionOverlap(Point new_p){
        //  if overlap, then return true, else return false
        if(!device_points.isEmpty()){
            for(Point p:device_points){
                int distance = (int)Math.sqrt(Math.pow(new_p.x - p.x, 2) + Math.pow(new_p.y - p.y, 2));
                Log.d(TAG, distance+"");
                if(distance < SEPRATION_DIST_THRESHOLD){
                    return true;
                }
            }
        }
        return false;
    }
}