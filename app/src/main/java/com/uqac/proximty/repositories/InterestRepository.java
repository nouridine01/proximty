package com.uqac.proximty.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;

import java.util.List;

public class InterestRepository {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void add(Interest interest){

    }

    public Interest getUser(){
        return null;
    }

    public List<Interest> getAll(){
        return null;
    }

    public void update(Interest interest){

    }

    public void delete(Interest interest){

    }


}
