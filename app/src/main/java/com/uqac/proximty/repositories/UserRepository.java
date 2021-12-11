package com.uqac.proximty.repositories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.uqac.proximty.PrefManager;
import com.uqac.proximty.callbacks.GetUserCallback;
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;

import java.io.ByteArrayOutputStream;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ExecutionException;


public class UserRepository {
    private UserDao userDao;
    AppDatabase appDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://proximty-d72e1.appspot.com");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user = null;

    List<CompletableFuture<User>> friendsList = new ArrayList<CompletableFuture<User>>();


    public UserRepository(Context context) {
        //this.userDao = AppDatabase.getDatabase(context).userDao();

    }

    public void getConnectedUser(String pseudo ,GetUserCallback userCallback){
        CompletableFuture<User> promise = getUserByPseudo(pseudo);
        promise.thenAccept(user1 -> {
            userCallback.onCallback(user1);
        });
    }

    public void add(User user) throws Exception {
        DocumentReference ref = db.collection("users").document(user.getPseudo());

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                if(u.getPseudo().equals("")){
                    // Add a new document
                    db.collection("users").document(user.getPseudo()).set(user);
                }else
                    try {
                        throw new Exception("pseudo exist");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }

        });

        /*DocumentReference newCityRef = db.collection("cities").document();
        users.setid(newCityref.getId());
        newCityRef.set(users);*/
    }

    public void getUser(String pseudo,GetUserCallback userCallback){

        DocumentReference docRef = db.collection("users").document(pseudo);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                userCallback.onCallback(u);
            }

        });

    }

    public CompletableFuture<User> getUserByPseudo(String pseudo){

        final CompletableFuture<User> promise = new CompletableFuture<>();

        DocumentReference docRef = db.collection("users").document(pseudo);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                promise.complete(u);
            }
        }).addOnFailureListener(command -> {
            promise.complete(null);
        });

        return promise;
    }

    public CompletableFuture<List<String>> getFriendsFromUserPseudo(String pseudo){
        final CompletableFuture<List<String>> promise = new CompletableFuture<>();

        DocumentReference docRef = db.collection("users").document(pseudo);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                promise.complete(u.getFriends());
            }
        }).addOnFailureListener(command -> {
            promise.complete(null);
        });

        return promise;
    }

    public CompletableFuture<User> connexion(String pseudo, String pwd){
        final CompletableFuture<User> promise = new CompletableFuture<>();
        db.collection("users").whereEqualTo("pseudo",pseudo).whereEqualTo("password",pwd)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    User u = document.toObject(User.class);
                    promise.complete(u);
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                    promise.complete(null);
                }
            }
        }).addOnFailureListener(command -> {
            promise.complete(null);
        });
        return  promise;
    }

    public List<Interest> getAll(){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return null;
    }

    public void update(User user){
        db.collection("users").document(user.getPseudo()).set(user);
    }

    public void delete(User user){
        db.collection("users").document(user.getPseudo())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void addImage(String name,Bitmap image){
        // Create a Cloud Storage reference from the app
        StorageReference storageRef = storage.getReference();
        // Create a reference to "mountains.jpg"
        StorageReference imRef = storageRef.child("profils/"+name);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }



    public CompletableFuture<Bitmap> getImage(String imageName){
        // Create a Cloud Storage reference from the app
        StorageReference storageRef = storage.getReference();
        StorageReference imRef = storageRef.child("profils/"+imageName);
        final CompletableFuture<Bitmap> promise = new CompletableFuture<>();

        final long ONE_MEGABYTE = 1024 * 1024;
        imRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                promise.complete(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                promise.complete(null);
            }
        });
        return promise;
    }

    public CompletableFuture<User> getUserByDeviceName(String name){

        final CompletableFuture<User> promise = new CompletableFuture<>();
        DocumentReference docRef = db.collection("users").document(name);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                promise.complete(u);
            }
        }).addOnFailureListener(command -> {
            promise.complete(null);
        });
        return promise;
    }


}
