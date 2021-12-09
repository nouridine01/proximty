package com.uqac.proximty.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uqac.proximty.MainActivity;
import com.uqac.proximty.R;
import com.uqac.proximty.adaptaters.ContactsAdapter;
import com.uqac.proximty.entities.User;
import com.uqac.proximty.models.Contact;
import com.uqac.proximty.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        UserRepository userRepository = new UserRepository(getContext());
        //récupération de la liste des pseudos
        MainActivity activity= (MainActivity) getActivity();
        CompletableFuture<List<String>> pseudoFriendList = userRepository.getFriendsFromUserPseudo(activity.getPrefManager().getUserPseudo());
        ArrayList<User> users = new ArrayList<User>();

        pseudoFriendList.thenAccept(pseudoList ->{
            //on affiche chaque user en fonction de leur pseudo
            int listSize = pseudoList.size() - 1;

            for(int i = 0; i < pseudoList.size(); i++) {
                CompletableFuture<User> myFriend = userRepository.getUserByPseudo(pseudoList.get(i));

                //affichage de leur pseudo en utilisant l'user

                int finalI = i;
                myFriend.thenAccept(user ->{
                    System.out.println("friend = " + user.getPseudo());

                    //ici tu remplies avec ta lsite de contact
                    users.add(user);

                    if(finalI == listSize){
                        // Create adapter passing in the sample user data
                        ContactsAdapter adapter = new ContactsAdapter(users);
                        // Attach the adapter to the recyclerview to populate items
                        rvContacts.setAdapter(adapter);
                    }

                });
            };
        });


        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        // That's all!
    }

}
