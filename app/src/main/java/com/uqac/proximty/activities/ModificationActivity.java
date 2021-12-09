package com.uqac.proximty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.entities.UserWithInterests;
import com.uqac.proximty.models.FlowLayout;
import com.uqac.proximty.repositories.UserRepository;

import java.io.IOException;
import java.util.List;

public class ModificationActivity extends AppCompatActivity {

    public final static int PICK_IMAGE = 1;

    private PrefManager prefManager;
    private UserRepository userRepository;
    private User user;
    Button modifPhoto, save;
    ImageView photo;
    EditText lastName, firstName, pseudo, password;
    FlowLayout interests;
    ImageButton addInterest;
    UserWithInterests userWithInterests;
    List<Interest> allInterests, userInterests;

    String photoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        prefManager = new PrefManager(this);
        userRepository = new UserRepository(this);
        user = userRepository.getConnectedUser(prefManager.getUserPseudo());
        //allInterests = AppDatabase.getDatabase(this).interestDao().getAll();
        //userWithInterests = AppDatabase.getDatabase(this).userDao().getUserWithInterests(user.getUid());

        initialSetup(findViewById(R.id.scrollLayout));
    }

    private void initialSetup(View view) {
        String uri = "@drawable/default_profile_pic";

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        photo = (ImageView) view.findViewById(R.id.photo);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable dr = getResources().getDrawable(imageResource);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable res = new BitmapDrawable(getResources(), bitmap);
        photo.setImageDrawable(res);

        modifPhoto = (Button) view.findViewById(R.id.modifPhoto);
        modifPhoto.setOnClickListener(view2 -> {
            // Open picture browser tab
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            // Set picture link in photoLink is made during return callback
        });

        lastName = (EditText) view.findViewById(R.id.nom);
        lastName.setText(user.getLastName());

        firstName = (EditText) view.findViewById(R.id.prenom);
        firstName.setText(user.getFirstName());

        pseudo = (EditText) view.findViewById(R.id.pseudo);
        pseudo.setText(user.getPseudo());

        password = (EditText) view.findViewById(R.id.password);
        password.setText(user.getPassword());

        interests = (FlowLayout) view.findViewById(R.id.interests);

        addInterest = (ImageButton) view.findViewById(R.id.addInterest);
        addInterest.setOnClickListener(view2 -> {
            addInterestSheetDialog(view);
        });

        userInterests = userWithInterests.interests;
        for (Interest interest : userInterests) {
            createDeleteInterest(view, interest);
        }

        save = (Button) view.findViewById(R.id.modify);
        save.setOnClickListener(view2 -> {
            user.setLastName(lastName.getText().toString());
            user.setFirstName(firstName.getText().toString());
            user.setPseudo(pseudo.getText().toString());
            user.setPassword(password.getText().toString());
            user.setPhoto(photoLink);

            //UserDao userDao = AppDatabase.getDatabase(this).userDao();
            //userDao.updateUsers(user);
            //userDao.insertUsersAndInterests(user, userInterests);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == PICK_IMAGE) {
            Uri photoUri = data.getData();
            Bitmap selectedImage = null;
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), photoUri);
                selectedImage = ImageDecoder.decodeBitmap(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable res = new BitmapDrawable(getResources(), selectedImage);
            photo.setImageDrawable(res);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createDeleteInterest(View view, Interest interest) {
        Button newInterest = new Button(this);

        newInterest.setOnClickListener(view1 -> {
            deleteInterestSheetDialog(view, interest, newInterest);
        });

        newInterest.setBackground(getResources().getDrawable(R.drawable.interest_button));
        newInterest.setText(interest.getName());

        interests.addView(newInterest, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void deleteInterestSheetDialog(View view, Interest interest, Button button) {
        final BottomSheetDialog sheet = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(view.getContext()).inflate(
                R.layout.delete_interest_sheet, view.findViewById(R.id.deleteInterestSheet));
        Button delete = sheetView.findViewById(R.id.buttonDeleteInterest);

        delete.setOnClickListener(view1 -> {
            FlowLayout flowView = view.findViewById(R.id.interests);

            userInterests.remove(interest);
            flowView.removeViewInLayout(button);
            sheet.dismiss();
        });

        sheet.setContentView(sheetView);
        sheet.show();
    }

    public void addInterestSheetDialog(View view) {
        final BottomSheetDialog sheet = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(view.getContext()).inflate(
                R.layout.add_interest_sheet, view.findViewById(R.id.addInterestSheet));
        FlowLayout layout = sheetView.findViewById(R.id.addInterest);

        for (Interest interest : allInterests) {
            if (!userInterests.contains(interest)) {
                createAddInterest(view, interest, layout);
            }
        }

        sheet.setContentView(sheetView);
        sheet.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createAddInterest(View view, Interest interest, FlowLayout flowView) {
        Button newInterest = new Button(this);

        newInterest.setOnClickListener(view2 -> {
            userInterests.add(interest);
            flowView.removeViewInLayout(newInterest);
            createDeleteInterest(view, interest);
        });

        newInterest.setBackground(getResources().getDrawable(R.drawable.interest_button));
        newInterest.setText(interest.getName());

        flowView.addView(newInterest, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}