package com.uqac.proximty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;

import java.util.List;

public class ProfileModif extends AppCompatActivity {

    User user;
    Button modifPhoto, save;
    ImageView photo;
    EditText lastName, firstName, pseudo, password;
    GridLayout interests;
    ImageButton addInterest;
    List<Interest> interestsList;

    String photoLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modif);

        photo = (ImageView) findViewById(R.id.photo);
        photo.setImageResource(getResources().getIdentifier(user.getPhoto(),"drawable",getPackageName()));

        modifPhoto = (Button) findViewById(R.id.modifPhoto);
        modifPhoto.setOnClickListener(view -> {
            // Open picture browser tab
            // Set picture link in photoLink
        });

        lastName = (EditText) findViewById(R.id.nom);
        lastName.setText(user.getLastName());

        firstName = (EditText) findViewById(R.id.prenom);
        firstName.setText(user.getFirstName());

        pseudo = (EditText) findViewById(R.id.pseudo);
        pseudo.setText(user.getPseudo());

        password = (EditText) findViewById(R.id.password);
        password.setText(user.getPassword());

        interests = (GridLayout) findViewById(R.id.interets);

        addInterest = (ImageButton) findViewById(R.id.addInteret);
        addInterest.setOnClickListener(view -> {
            // Open interest browser tab
        });

        /*interestsList = user.getInterets();
        for (Interest interet : interestsList) {
            createInterest(interet);
        }*/

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(view -> {
            user.setLastName(lastName.getText().toString());
            user.setFirstName(firstName.getText().toString());
            user.setPseudo(pseudo.getText().toString());
            user.setPassword(password.getText().toString());
            user.setPhoto(photoLink);
        });
    }

    private void createInterest(Interest interet) {
        Button newInterest = new Button(this);

        // Set the size of the button
        //newInterest.setHeight();
        //newInterest.setWidth(wrap_content);

        newInterest.setOnClickListener(view -> {
            // Open delete proposition tab
        });

        interests.addView(newInterest, 0);
    }
}