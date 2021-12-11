package com.uqac.proximty.repositories;


import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.uqac.proximty.entities.MNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class NotificationRepository {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void add(MNotification notification){
        DocumentReference newNotificationRef = db.collection("notification").document();
        notification.setId(newNotificationRef.getId());
        newNotificationRef.set(notification);
    }

    public CompletableFuture<List<String>> getNotificationsIdFromUserPseudo(String pseudo){
        final CompletableFuture<List<String>> promise = new CompletableFuture<>();
        List<String> notifId = new ArrayList<String>();

        db.collection("notification").whereEqualTo("receverId", pseudo)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            notifId.add(document.getId());
                        }
                    } else {
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    promise.complete(notifId);
                }
        });

        return promise;
    }


    public CompletableFuture<MNotification> getNotificationFromId(String Id){
        final CompletableFuture<MNotification> promise = new CompletableFuture<>();

        DocumentReference docRef = db.collection("notification").document(Id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MNotification n = documentSnapshot.toObject(MNotification.class);
                promise.complete(n);
            }
        }).addOnFailureListener(command -> {
            promise.complete(null);
        });

        return promise;
    }

    public void update(MNotification notification, MNotification newNotification){
        db.collection("notification").document(notification.getId()).set(newNotification);
    }

    public void accept(MNotification notification){
        DocumentReference docRef = db.collection("notification").document(notification.getId());

        docRef
                .update("accepted", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void setIsPending(MNotification notification, Boolean value){
        DocumentReference docRef = db.collection("notification").document(notification.getId());

        docRef
                .update("accepted", value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void delete(MNotification notification){

        db.collection("messages").document(notification.getId())
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

}
