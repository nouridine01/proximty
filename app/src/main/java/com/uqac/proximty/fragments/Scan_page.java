package com.uqac.proximty.fragments;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.LocationManager;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import android.os.Build;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;

import androidx.core.widget.TextViewCompat;

import androidx.fragment.app.Fragment;

import com.skyfishjy.library.RippleBackground;
import com.uqac.proximty.MainActivity;
import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.uqac.proximty.activities.ChatActivity;
import com.uqac.proximty.entities.MNotification;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.repositories.NotificationRepository;
import com.uqac.proximty.repositories.UserRepository;
import com.uqac.proximty.sockets.Client;
import com.uqac.proximty.sockets.ServeurMT;


public class Scan_page<MapList> extends Fragment implements  WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    public static String pseudo="";
    public static ArrayList<String> interests=new ArrayList<>();
    public static Bitmap image=null;
    public static View bottomSheetView;

    private static final String TAG = "Scan";
    private static final int SEPRATION_DIST_THRESHOLD = 50;

    private static int device_count = 0;

    RippleBackground rippleBackground;
    ImageView centerDeviceIcon;
    ArrayList<Point> device_points = new ArrayList<>();
    ArrayList<View> views = new ArrayList<>();


    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private WifiP2pDevice device;

    Map<String,Socket> sockets = new HashMap<String,Socket>();

    private boolean scan = true;
    PrefManager prefManager;



    public List<WifiP2pDevice> getPeers() {
        return peers;
    }

    public void setPeers(List<WifiP2pDevice> peers) {
        this.peers = peers;
    }


    private WifiP2pDevice deviceToConnected;
    private WifiP2pInfo info;
    private View deviceClickedView=null;

    public ServeurMT serveurMT;

    UserRepository userRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        return inflater.inflate(R.layout.scan, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialSetup(view);
        scan = true;
    }


    public void discover() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        MainActivity activity= (MainActivity) getActivity();
        activity.getManager().discoverPeers(activity.getChannel(), new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getActivity(), "Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initialSetup(View view) {

        serveurMT = new ServeurMT(getActivity());
        userRepository=new UserRepository(getActivity());
        prefManager=new PrefManager(getActivity());
        // layout files
        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        centerDeviceIcon = (ImageView) view.findViewById(R.id.centerImage);
        // add onClick Listeners
        centerDeviceIcon.setOnClickListener(v -> {
            if(scan){
                rippleBackground.startRippleAnimation();
                serveurMT.start();
                discover();
                scan=false;
                Toast.makeText(getActivity(),  Settings.Global.getString(getActivity().getContentResolver(), "device_name"), Toast.LENGTH_SHORT).show();
            }

            //showUserDetailDialo(view);
        });

        // center button position
        Display display = getActivity().getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        device_points.add(new Point(size.x / 2, size.y / 2));


        Log.d("MainActivity", size.x + "  " + size.y);

    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {

        this.info = info;


        // The owner IP is now known.

        // After the group negotiation, we assign the group owner as the file
        // server. The file server is single threaded, single connection server
        // socket.
        if (info.groupFormed && info.isGroupOwner) {
            Toast.makeText(getActivity(), "final step. client "+info.groupOwnerAddress, Toast.LENGTH_SHORT).show();
            //comportement client
            //Socket serveur = null;
            //showUserDetailDialo(deviceClickedView);
            /*try {
                serveur = new Socket(info.groupOwnerAddress, ServeurMT.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Client client= new Client(serveur,getActivity(),true);

            //client.start();*/


        } else if (info.groupFormed) {

            Toast.makeText(getActivity(), "final step. server", Toast.LENGTH_SHORT).show();
        }
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

    /*@Override
    public void onClick(View view) {



        rippleBackground.startRippleAnimation();

        discover();

    }*/

    private void connect(View view){
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceToConnected.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        deviceClickedView = view;
        ((DeviceActionListener) getActivity()).connect(config);

    }

    private void showUserDetailDialo(View view) {

        MNotification notification = new MNotification();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                view.getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(view.getContext())
                .inflate(R.layout.layout_bottom_sheet,
                        (LinearLayout)view.findViewById(R.id.bottomSheetContainer));
        bottomSheetView.findViewById(R.id.imageViewCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Shared!!!" +view.getId(), Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetView.findViewById(R.id.imageViewConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(view.getContext(), ChatActivity.class));
                //doit creer une notification

                NotificationRepository notificationRepository = new NotificationRepository();
                notificationRepository.add(notification);

                Toast.makeText(view.getContext(), "Demande de relation envoyée", Toast.LENGTH_SHORT).show();

            }
        });

        ImageView imageView = bottomSheetView.findViewById(R.id.imageProfil);

        CompletableFuture<User> pr= userRepository.getUserByDeviceName(deviceToConnected.deviceName);
        pr.thenAccept(u->{
            Log.e("test firebase async", u.toString());
            userRepository.getImage(u.getPhoto()).thenAccept(im->{
                if(im!=null)
                    imageView.setImageBitmap((Bitmap) im);
                else imageView.setImageResource(R.drawable.email);
            });

            TextView textPseudo=bottomSheetView.findViewById(R.id.txtNameAnonym);
            textPseudo.setText(u.getPseudo());

            u.getInterests().forEach(i->{
                GridLayout gridLayoutInterte = bottomSheetView.findViewById(R.id.grid_interet);
                TextView textInteret=  new TextView(view.getContext(), null, 0, R.style.ButtonInteret);
                textInteret.setText(i);
                gridLayoutInterte.addView(textInteret);
            });

            notification.setPseudo(prefManager.getUserPseudo());
            notification.setAccepted(false);
            notification.setPending(true);
            notification.setReceverId(u.getPseudo());
            notification.setSenderId(prefManager.getUserPseudo());
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
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
        device1.setOnClickListener(v -> {
            if(peers.size()>0){
                deviceToConnected=peers.stream().filter(p->p.deviceName==device_name).findFirst().get();
                Toast.makeText(getContext(), deviceToConnected.deviceName+" = "+deviceToConnected.deviceAddress, Toast.LENGTH_SHORT).show();
            }
            //connect(device1);
            showUserDetailDialo(device1);

        });
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

    //------------------------------------------------
    public WifiP2pDevice getDevice() {
        return device;
    }

    private static String getDeviceStatus(int deviceStatus) {
        Log.d(MainActivity.TAG, "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }



    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        views.forEach(view -> rippleBackground.removeView(view));
        peers.clear();
        peers.addAll(peerList.getDeviceList());

        if (peers.size() == 0) {
            Log.d(MainActivity.TAG, "No devices found");
            return;
        }


        peers.forEach(p->{
            View tmp_device = createNewDevice(p.deviceName);
            views.add(tmp_device);
            rippleBackground.addView(tmp_device);
            foundDevice(tmp_device);
        });

    }

    public void clearPeers() {
        peers.clear();
        //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    /**
     * An interface-callback for the activity to listen to fragment interaction
     * events.
     */
    public interface DeviceActionListener {

        void showDetails(WifiP2pDevice device);

        void cancelDisconnect();

        void connect(WifiP2pConfig config);

        void disconnect();
    }


}