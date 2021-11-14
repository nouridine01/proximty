package com.uqac.proximty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText inputPseudo;
    private TextInputEditText inputLastName;
    private TextInputEditText inputFirstName;
    private TextInputEditText inputPassword;
    private TextInputEditText inputCPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputPseudo = findViewById(R.id.input_pseudo);
        inputLastName = findViewById(R.id.input_lastname);
        inputFirstName = findViewById(R.id.input_firstname);
        inputPassword = findViewById(R.id.input_password_up);
        inputCPassword = findViewById(R.id.input_cpassword_up);
    }

    /**
     * La fonction recupere les informations de l'utilisateur pour l'enregistrer dans une base donnée distant
     * Si l'eregistrement est bien effectuer alors retourne True sinon False
     * @return
     */
    public boolean register(){
        String pseudo = inputPseudo.getText().toString();
        String lastName = inputLastName.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String password = inputPassword.getTag().toString();
        String confirmPassword = inputCPassword.getText().toString();

        //Function de signUp

        return false;
    }

    public void sigINButton(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }
}