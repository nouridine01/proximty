package com.uqac.proximty.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uqac.proximty.R;
import com.uqac.proximty.adaptaters.ContactsAdapter;
import com.uqac.proximty.fragments.Contact_page;
import com.uqac.proximty.fragments.Scan_page;
import com.uqac.proximty.models.Contact;

import java.util.ArrayList;

public class TabActivity  extends AppCompatActivity {

    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

    }

    public void selectFrag(View view) {
    }
}
