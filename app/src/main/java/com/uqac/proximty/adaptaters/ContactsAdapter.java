package com.uqac.proximty.adaptaters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.uqac.proximty.activities.ChatActivity;
import com.uqac.proximty.models.Contact;
import com.uqac.proximty.R;

import java.util.List;

public class ContactsAdapter extends
        RecyclerView.Adapter<ContactsAdapter.ViewHolder> {


    // Store a member variable for the contacts
    private List<Contact> mContacts;

    // Pass in the contact array into the constructor
    public ContactsAdapter(List<Contact> contacts) {
        mContacts = contacts;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageView;
        public TextView nameTextView, lastMessage;
        public TextView txtStatus;
        CardView cardSelect;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            lastMessage = (TextView) itemView.findViewById(R.id.lastMessage);
            txtStatus = (TextView) itemView.findViewById(R.id.textStatus);
            cardSelect = itemView.findViewById(R.id.cardviewContact);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        ImageView imageprofile = holder.imageView;
        //TODO recuperer l'image ici
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageprofile);
        textView.setText(contact.getName());
        TextView textStatus = holder.txtStatus;
        if(contact.isOnline() ){
            textStatus.setText("En ligne");//            textStatus.setText(contact.isOnline() ? "En Ligne" : "Hors Ligne");
            textStatus.setTextColor(R.color.primaryDarkColor);
        }else {
            textStatus.setText("Hors ligne");
        }

        /**
         * La personne une personne qui equivaut à une personne ayant accepter de chater,
         * la liste des contacts est charger et chaque contact doit contenir obligatoirement
         * l'identifiant firebase de ce contact.
         */
        holder.cardSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clique sur le "+contact.getName());
                Intent intent =  new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("receiveruid",contact.getFirebaseId());
                intent.putExtra("name",contact.getName());
               view.getContext().startActivity(intent);
            }
        });

        holder.cardSelect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("Long clique sur le "+contact.getName());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}