package com.uqac.proximty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputPseudo;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputPseudo = findViewById(R.id.input_pseudo);
        inputPassword = findViewById(R.id.input_password);

    }

    public void loginButton(View view) {

        String pseudo = inputPseudo.getText().toString();
        String password = inputPassword.getText().toString();
        System.out.println(pseudo+ "  "+password);
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void sigUpButton(View view) {
        startActivity(new Intent(this,SignUpActivity.class));
    }
}