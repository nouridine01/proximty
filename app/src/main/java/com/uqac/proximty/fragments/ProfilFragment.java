package com.uqac.proximty.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.activities.LoginActivity;
import com.uqac.proximty.activities.ModificationActivity;
import com.uqac.proximty.callbacks.GetUserCallback;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.InterestDao;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.dao.UserInterestCrossRefDao;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserWithInterests;
import com.uqac.proximty.models.FlowLayout;
import com.uqac.proximty.repositories.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    private PrefManager prefManager;
    private UserRepository userRepository;
    private User user;
    Button modify, disconnect;
    ImageView photo;
    TextView lastName, firstName, pseudo;
    FlowLayout interests;
    List<String> userInterests;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(getActivity());
        userRepository = new UserRepository(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRepository.getConnectedUser(prefManager.getUserPseudo(), new GetUserCallback() {
            @Override
            public void onCallback(User u) {
                user=u;
                initialSetup(view);
            }
        });

    }

    private void initialSetup(View view) {
        String uri = "@drawable/default_profile_pic";

        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());

        photo = (ImageView) view.findViewById(R.id.photo);

        userRepository.getImage(user.getPhoto()).thenAccept(im->{
            if(im!=null)
                photo.setImageBitmap((Bitmap) im);
            else photo.setImageResource(R.drawable.default_profile_pic);
        });

        lastName = (TextView) view.findViewById(R.id.nom);
        lastName.setText(user.getLastName());

        firstName = (TextView) view.findViewById(R.id.prenom);
        firstName.setText(user.getFirstName());

        pseudo = (TextView) view.findViewById(R.id.pseudo);
        pseudo.setText(user.getPseudo());

        interests = (FlowLayout) view.findViewById(R.id.interests);

        userInterests = user.getInterests();
        for (String interest : userInterests) {
            createInterest(interest);
        }

        modify = (Button) view.findViewById(R.id.modify);
        modify.setOnClickListener(view2 -> {
            startActivity(new Intent(getActivity(), ModificationActivity.class));
        });

        disconnect = (Button) view.findViewById(R.id.disconnect);
        disconnect.setOnClickListener(view2 -> {
            prefManager.setUserConnected(false);
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createInterest(String interest) {
        Button newInterest = new Button(getActivity());

        newInterest.setBackground(getResources().getDrawable(R.drawable.interest_button));
        newInterest.setText(interest);

        interests.addView(newInterest, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}