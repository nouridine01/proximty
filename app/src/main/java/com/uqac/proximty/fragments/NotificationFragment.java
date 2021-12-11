package com.uqac.proximty.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyfishjy.library.RippleBackground;
import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.adaptaters.NotificationAdapter;
import com.uqac.proximty.entities.MNotification;
import com.uqac.proximty.repositories.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PrefManager prefManager;


    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        prefManager = new PrefManager(getActivity());
        super.onViewCreated(view, savedInstanceState);
        initialSetup(view);
    }

    private void initialSetup(View view) {
        RecyclerView  notificationRecycle=  view.findViewById(R.id.notificationContact);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        notificationRecycle.setLayoutManager(linearLayoutManager); // s

        NotificationRepository notificationRepository = new NotificationRepository();

        CompletableFuture<List<String>> idNotificationList = notificationRepository.getNotificationsIdFromUserPseudo(prefManager.getUserPseudo());
        List<MNotification> notificationList = new ArrayList<>();


        /*notificationList.add(new MNotification("niki","rnie.png"));
        notificationList.add(new MNotification("miki","rnie.png"));
        MNotification mn=new MNotification("Dada Kmaog","rnie.png");
        mn.setAccepted(true);
        mn.setPending(true);
        notificationList.add(mn);*/

        idNotificationList.thenAccept(idList ->{
            //on affiche chaque user en fonction de leur pseudo
            int listSize = idList.size() - 1;

            for(int i = 0; i < idList.size(); i++) {
                CompletableFuture<MNotification> myNotification = notificationRepository.getNotificationFromId(idList.get(i));

                //affichage de leur pseudo en utilisant l'user

                int finalI = i;
                myNotification.thenAccept(notif ->{

                    //ici tu remplies avec ta lsite de contact
                    notificationList.add(notif);

                    if(finalI == listSize){
                        NotificationAdapter customAdapter = new NotificationAdapter(notificationList);
                        notificationRecycle.setAdapter(customAdapter); // set the Adapter to RecyclerView
                    }

                });
            };
        });

    }

}