package com.uqac.proximty.adaptaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uqac.proximty.R;
import com.uqac.proximty.entities.MNotification;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Copyright (c) 2021, NIKISS. All Rights Reserved.
 * <p>
 * Save to the extent permitted by law, you may not use, copy, modify, distribute or
 * create derivative works of this material or any part of it without the prior
 * written consent of  OUEDRAOGO ISSOUF NIKISS.email:ouedraogo.nikiss@gmail.com
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the software.
 * Created on 27,novembre,2021
 */
public class NotificationAdapter extends RecyclerView.Adapter{

    private List<MNotification> notificationList;
    int ITEM_PENDING=1;
    int ITEM_ACCEPTED=2;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==ITEM_PENDING)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rownotiflayoutpending, parent, false);
            return new NotificationAdapter.PendingViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rownotiflayoutaccepted, parent, false);
            return new NotificationAdapter.AcceptedHolder(v);
        }
    }


    @Override
    public int getItemViewType(int position) {
        //TODO:: A changer
        MNotification notification=notificationList.get(position);

        if(notification!=null && notification.isPending() && !notification.isAccepted())
        {
            return ITEM_PENDING;
        }
        else
        {
            return  ITEM_ACCEPTED;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MNotification notification = notificationList.get(position);

        if(holder.getClass()== NotificationAdapter.AcceptedHolder.class)
        {
            NotificationAdapter.AcceptedHolder viewHolder=(NotificationAdapter.AcceptedHolder)holder;
            //viewHolder.textViewmessaage.setText(messages.getMessage());
            viewHolder.textViewPseudo.setText(notification.getPseudo());

        }
        else
        {
            NotificationAdapter.PendingViewHolder viewHolder=(NotificationAdapter.PendingViewHolder)holder;
            viewHolder.textViewPseudo.setText(notification.getPseudo());

            viewHolder.confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:: envoyer a ce niveau le message envoyer par la personne
                    System.out.println("Confirm button for:"+ notification.getPseudo());
                }
            });
            viewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("Clieck cancel button for:"+ notification.getPseudo());
                }
            });
        }

        //holder.imageView.setImageResource(R.drawable.bottom_sheet_back);
        //holder.textViewPseudo.setText(notification.getPseudo());
    }

    public NotificationAdapter(List<MNotification> notificationList) {

        this.notificationList = notificationList;
    }



    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewPseudo;// init the item view's
        public ImageView imageView ;
        public Button confirmButton, cancelButton;
        public PendingViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            textViewPseudo = (TextView) itemView.findViewById(R.id.pseudo);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            confirmButton =  itemView.findViewById(R.id.buttonConfirm);
            cancelButton = itemView.findViewById(R.id.buttonCancel);

        }
    }

    public class AcceptedHolder extends RecyclerView.ViewHolder {
        public TextView textViewPseudo;// init the item view's
        public ImageView imageView ;
        public AcceptedHolder(View itemView) {
            super(itemView);
            textViewPseudo = (TextView) itemView.findViewById(R.id.pseudo);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}