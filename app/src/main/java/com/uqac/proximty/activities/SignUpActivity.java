package com.uqac.proximty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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


public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText inputPseudo;
    private TextInputEditText inputLastName;
    private TextInputEditText inputFirstName;
    private TextInputEditText inputPassword;
    private TextInputEditText inputCPassword;
    AppDatabase appDatabase;
    UserDao userDao;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputPseudo = findViewById(R.id.input_pseudo_su);
        inputLastName = findViewById(R.id.input_lastname);
        inputFirstName = findViewById(R.id.input_firstname);
        inputPassword = findViewById(R.id.input_password_up);
        inputCPassword = findViewById(R.id.input_cpassword_up);
        prefManager = new PrefManager(this);
    }

    /**
     * La fonction recupere les informations de l'utilisateur pour l'enregistrer dans une base donnée distant
     * Si l'eregistrement est bien effectuer alors retourne True sinon False
     * @return
     */

    public void sigINButton(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void register(View view) {
        String pseudo = inputPseudo.getText().toString();
        String lastName = inputLastName.getText().toString();
        String firstName = inputFirstName.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputCPassword.getText().toString();
        //userDao=AppDatabase.getDatabase(this).userDao();
        //Function de signUp
        User u = new User();
        u.setLastName(lastName);
        u.setFirstName(firstName);
        u.setPseudo(pseudo);
        u.setPhoto("default_profile_pic");
        if(password.equals(confirmPassword)){
            u.setPassword(password);
            //userDao.insertUsers(u);
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            Toast.makeText(this,"les mots de passes ne correspondent pas",Toast.LENGTH_LONG).show();
        }
    }
}