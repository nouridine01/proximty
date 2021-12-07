package com.uqac.proximty.repositories;

import android.content.Context;
import android.graphics.Bitmap;

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
import com.uqac.proximty.dao.AppDatabase;
import com.uqac.proximty.dao.UserDao;
import com.uqac.proximty.entities.Interest;
import com.uqac.proximty.entities.User;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private UserDao userDao;
    AppDatabase appDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://proximty-d72e1.appspot.com");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserRepository(Context context) {
        this.userDao = AppDatabase.getDatabase(context).userDao();

    }

    public User getConnectedUser(long id){
        return userDao.getUserById(id);
    }

    public void add(User user) throws Exception {
        DocumentReference ref = db.collection("users").document(user.getPseudo());
        if(ref == null){
            // Add a new document
            db.collection("users").document(user.getPseudo()).set(user);
        }else throw new Exception("pseudo exist");
        /*DocumentReference newCityRef = db.collection("cities").document();
        newCityRef.set(users);*/
    }

    public User getUser(String pseudo){
        final User[] user = {null};
        DocumentReference docRef = db.collection("users").document(pseudo);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User u = documentSnapshot.toObject(User.class);
                user[0] = u;
            }

        });
        return user[0];
    }

    public User connexion(String pseudo, String pwd){
        final User[] user = {null};
        db.collection("users").whereEqualTo("pseudo",pseudo).whereEqualTo("password",pwd)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    User u = document.toObject(User.class);
                    user[0] = u;
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return user[0];
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
        StorageReference imRef = storageRef.child("profils/"+name+".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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



    public Bitmap getImage(String imageName){
        // Create a Cloud Storage reference from the app
        StorageReference storageRef = storage.getReference();
        StorageReference imRef = storageRef.child("profils/"+imageName+".jpg");

        Bitmap image = null;
        final long ONE_MEGABYTE = 1024 * 1024;
        imRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        return image;
    }

    public User getUserByDeviceName(String deviceName){
        final User[] user = {null};
        db.collection("users").whereEqualTo("deviceName",deviceName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    User u = document.toObject(User.class);
                    user[0] = u;
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return user[0];
    }



}
