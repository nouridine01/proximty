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
import com.uqac.proximty.MainActivity;
import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.models.FlowLayout;
import com.uqac.proximty.repositories.InterestRepository;
import com.uqac.proximty.repositories.UserRepository;

import java.io.IOException;
import java.util.List;

public class ModificationActivity extends AppCompatActivity {

    public final static int PICK_IMAGE = 1;

    private User user;
    private ImageView photo;
    private FlowLayout interests;
    private List<Interest> allInterests;
    private List<String> userInterests;
    private Bitmap userPicture;
    private boolean dirtyBitPic = false;

    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        PrefManager prefManager = new PrefManager(this);

        InterestRepository interestRepository = new InterestRepository();
        interestRepository.getAll().thenAccept(list -> {
            allInterests = list;

            userRepository = new UserRepository(this);
            userRepository.getConnectedUser(prefManager.getUserPseudo(), u -> {
                user=u;
                initialSetup(findViewById(R.id.scrollLayout));
            });
        });
    }

    private void initialSetup(View view) {

        photo = view.findViewById(R.id.photo);
        userRepository.getImage(user.getPhoto()).thenAccept(im->{
            if(im!=null)
                photo.setImageBitmap(im);
            else photo.setImageResource(R.drawable.default_profile_pic);
        });

        Button modifPhoto = view.findViewById(R.id.modifPhoto);
        modifPhoto.setOnClickListener(view2 -> {
            // Open picture browser tab
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            // Set picture link in photoLink is made during return callback
        });

        EditText lastName = view.findViewById(R.id.nom);
        lastName.setText(user.getLastName());

        EditText firstName = view.findViewById(R.id.prenom);
        firstName.setText(user.getFirstName());

        EditText pseudo = view.findViewById(R.id.pseudo);
        pseudo.setText(user.getPseudo());

        EditText password = view.findViewById(R.id.password);
        password.setText(user.getPassword());

        interests = view.findViewById(R.id.interests);

        ImageButton addInterest = view.findViewById(R.id.addInterest);
        addInterest.setOnClickListener(view2 -> addInterestSheetDialog(view));

        userInterests = user.getInterests();
        for (Interest interest : allInterests) {
            if (userInterests.contains(interest.getName())) {
                createDeleteInterest(view, interest);
            }
        }

        Button save = view.findViewById(R.id.saveProfile);
        save.setOnClickListener(view2 -> {
            user.setLastName(lastName.getText().toString());
            user.setFirstName(firstName.getText().toString());
            user.setPseudo(pseudo.getText().toString());
            user.setPassword(password.getText().toString());

            if (dirtyBitPic) {
                userRepository.addImage(user.getPseudo(), userPicture);
                user.setPhoto(user.getPseudo());
            }
            userRepository.update(user);

            startActivity(new Intent(this, MainActivity.class));
        });

        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> startActivity(new Intent(this, MainActivity.class)));
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
            userPicture = selectedImage;
            dirtyBitPic = true;
            Drawable res = new BitmapDrawable(getResources(), selectedImage);
            photo.setImageDrawable(res);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createDeleteInterest(View view, Interest interest) {
        Button newInterest = new Button(this);

        newInterest.setOnClickListener(view1 -> deleteInterestSheetDialog(view, interest, newInterest));

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

            userInterests.remove(interest.getName());
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
            if (!userInterests.contains(interest.getName())) {
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
            userInterests.add(interest.getName());
            flowView.removeViewInLayout(newInterest);
            createDeleteInterest(view, interest);
        });

        newInterest.setBackground(getResources().getDrawable(R.drawable.interest_button));
        newInterest.setText(interest.getName());

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(params);
        marginParams.setMargins(16, 16, 16, 16);
        newInterest.setLayoutParams(marginParams);
        flowView.addView(newInterest, marginParams);
    }

}