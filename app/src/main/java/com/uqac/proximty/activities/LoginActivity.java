package com.uqac.proximty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import com.uqac.proximty.MainActivity;
import com.uqac.proximty.PrefManager;
import com.uqac.proximty.R;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.fragments.Scan_page;
import com.uqac.proximty.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputPseudo;
    private TextInputEditText inputPassword;
    AppDatabase appDatabase;
    UserDao userDao;
    private PrefManager prefManager;

    private UserRepository userRepository;

    public PrefManager getPrefManager() {
        return prefManager;
    }

    public void setPrefManager(PrefManager prefManager) {
        this.prefManager = prefManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputPseudo = findViewById(R.id.input_pseudo);
        inputPassword = findViewById(R.id.input_password);
        prefManager = new PrefManager(this);
        userRepository = new UserRepository(this);
    }

    public void loginButton(View view) {

        String pseudo = inputPseudo.getText().toString();
        String password = inputPassword.getText().toString();

        CompletableFuture<User> promise = userRepository.connexion(pseudo, password);
        promise.thenAccept(u ->{
            if(u != null){
                prefManager.setFirstTimeLaunch(false);
                prefManager.setUserPseudo(u.getPseudo());
                prefManager.setUserConnected(true);
                startActivity(new Intent(this, MainActivity.class));
            }else {
                Toast.makeText(this,"identifiant invalide",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sigUpButton(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
    }
}