package com.uqac.proximty.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.dao.UserInterestCrossRefDao;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserInterestCrossRef;
import com.uqac.proximty.models.FlowLayout;
import com.uqac.proximty.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    private PrefManager prefManager;
    private UserRepository userRepository;
    private User user;
    Button modifPhoto, save;
    ImageView photo;
    EditText lastName, firstName, pseudo, password;
    FlowLayout interests;
    ImageButton addInterest;
    List<Interest> allInterests, userInterests;

    String photoLink;


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
        user = userRepository.getConnectedUser(prefManager.getUserId());
        allInterests = AppDatabase.getDatabase(getActivity()).interestDao().getAll();
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
        initialSetup(view);
    }

    private void initialSetup(View view) {
        String uri = "@drawable/default_profile_pic";

        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());

        photo = (ImageView) view.findViewById(R.id.photo);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable dr = getResources().getDrawable(imageResource);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable res = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 125, 125, true));
        photo.setImageDrawable(res);

        modifPhoto = (Button) view.findViewById(R.id.modifPhoto);
        modifPhoto.setOnClickListener(view2 -> {
            // Open picture browser tab
            // Set picture link in photoLink
        });

        lastName = (EditText) view.findViewById(R.id.nom);
        lastName.setText(user.getLastName());

        firstName = (EditText) view.findViewById(R.id.prenom);
        firstName.setText(user.getFirstName());

        pseudo = (EditText) view.findViewById(R.id.pseudo);
        pseudo.setText(user.getPseudo());

        password = (EditText) view.findViewById(R.id.password);
        password.setText(user.getPassword());

        interests = (FlowLayout) view.findViewById(R.id.interets);

        addInterest = (ImageButton) view.findViewById(R.id.addInteret);
        addInterest.setOnClickListener(view2 -> {
            // Open interest browser tab
            String interest = "Lecture";
            createInterest(interest);
        });

        userInterests = getUserInterests();
        for (Interest interest : userInterests) {
            createInterest(interest.getName());
        }

        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(view2 -> {
            user.setLastName(lastName.getText().toString());
            user.setFirstName(firstName.getText().toString());
            user.setPseudo(pseudo.getText().toString());
            user.setPassword(password.getText().toString());
            user.setPhoto(photoLink);

            UserDao userDao = AppDatabase.getDatabase(getActivity()).userDao();
            userDao.updateUsers(user);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createInterest(String interest) {
        Button newInterest = new Button(getActivity());

        newInterest.setOnClickListener(view -> {
            // Open delete proposition tab
        });

        newInterest.setBackground(getResources().getDrawable(R.drawable.interest_button));
        newInterest.setText(interest);

        interests.addView(newInterest, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private List<Interest> getUserInterests() {
        List<Interest> userInterests = new ArrayList<>();

        UserInterestCrossRefDao dao = AppDatabase.getDatabase(getActivity()).userInterestCrossRefDao();
        // boucle for qui traverse tous les éléments du dao
        // UserInterestCrossRef crossRef = ...
        // ajout à la liste les interets dont un élément crossref(element,user) existe

        return userInterests;
    }

    /*@Override
    public void onResume() {
        System.out.println("Resuming");
        super.onResume();
    }

    @Override
    public void onPause() {
        System.out.println("Pausing");
        super.onPause();
    }

    @Override
    public void onStart() {
        System.out.println("Starting");
        super.onStart();
    }

    @Override
    public void onStop() {
        System.out.println("Stopping");
        super.onStop();
    }*/

    /*@Override
    public void onAttach() {
        System.out.println("Attaching");
        super.onAttach();
    }*/
}