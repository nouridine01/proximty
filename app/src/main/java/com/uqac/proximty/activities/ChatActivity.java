package com.uqac.proximty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.squareup.picasso.Picasso;
import com.uqac.proximty.R;
import com.uqac.proximty.adaptaters.MessagesAdapter;
import com.uqac.proximty.models.Messages;

public class ChatActivity extends AppCompatActivity {

    EditText mgetmessage;
    ImageButton msendmessagebutton;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    ImageView mimageviewofspecificuser;
    TextView mnameofspecificuser;

    private String enteredmessage;
    Intent intent;
    String mrecievername,sendername,mrecieveruid,msenderuid;
    //private FirebaseAuth firebaseAuth;
    //FirebaseDatabase firebaseDatabase;
    String senderroom,recieverroom;

    ImageButton mbackbuttonofspecificchat;

    RecyclerView mmessagerecyclerview;

    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.carviewofsendmessage);
        msendmessagebutton=findViewById(R.id.imageviewsendmessage);
        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);
        mnameofspecificuser=findViewById(R.id.Nameofspecificuser);
        mimageviewofspecificuser=findViewById(R.id.specificuserimageinimageview);
        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);

        messagesArrayList=new ArrayList<>();
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(ChatActivity.this,messagesArrayList);
        mmessagerecyclerview.setAdapter(messagesAdapter);

        intent=getIntent();
        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Toolbar is Clicked",Toast.LENGTH_SHORT).show();


            }
        });

        //firebaseAuth=FirebaseAuth.getInstance();
        //firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        msenderuid= "01";//firebaseAuth.getUid();
        //mrecieveruid=getIntent().getStringExtra("receiveruid");
        //mrecievername=getIntent().getStringExtra("name");

        senderroom=msenderuid+mrecieveruid;
        recieverroom=mrecieveruid+msenderuid;


        //DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderroom).child("messages");

        messagesAdapter=new MessagesAdapter(ChatActivity.this,messagesArrayList);

        Messages messages=new Messages("hello","01",3,"10h22");
        messagesArrayList.add(messages);
        messages=new Messages("Comment tu vas ?","01",3,"10h23");
        messagesArrayList.add(messages);
        messages=new Messages("Je vais bieb et toi ?","03",3,"1637649973000");
        messagesArrayList.add(messages);
        
        messages=new Messages("Ok super","01",3,"10h23");

        messagesArrayList.add(messages);

        messagesAdapter.notifyDataSetChanged();

        /**
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Messages messages=snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         **/



        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mnameofspecificuser.setText(mrecievername);
        //String uri=intent.getStringExtra("imageuri");
        //String uri=intent.getStringExtra("imageuri");
        if(false)
        {
            Toast.makeText(getApplicationContext(),"null is recieved",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Picasso.get().load(uri).into(mimageviewofspecificuser);
            Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(mimageviewofspecificuser);
        }


        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=mgetmessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    /**

                    Date date=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    Messages messages=new Messages(enteredmessage,firebaseAuth.getUid(),date.getTime(),currenttime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(recieverroom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    mgetmessage.setText(null);

                     **/
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}

/**
 * https://www.youtube.com/watch?v=4yADIs5b1No
 * https://github.com/Brijesh-kumar-sharma/chatAppInAndroidStudio/blob/master/app/src/main/res/values/themes.xml
 */

