package com.uqac.proximty.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uqac.proximty.R;
import com.uqac.proximty.adaptaters.ContactsAdapter;
import com.uqac.proximty.models.Contact;

import java.util.ArrayList;

public class Contact_page extends Fragment {


    public Contact_page() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_users, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate  (savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateContacts(view);
    }

    public void populateContacts(View view) {
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);

        // Initialize contacts
        ArrayList<Contact> contacts = Contact.createContactsList(20);
        // Create adapter passing in the sample user data
        ContactsAdapter adapter = new ContactsAdapter(contacts);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        // That's all!
    }

}
