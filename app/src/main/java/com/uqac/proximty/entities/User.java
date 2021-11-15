package com.uqac.proximty.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import dagger.Component;
import dagger.hilt.DefineComponent;

@Entity(indices = {@Index(value = {"pseudo"},
        unique = true)})

public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long uid;

    private String pseudo;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private String password;

    private String photo;

    //private List<Interest> interests;

    public User() {}

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /*public List<Interest> getInterets() { return this.interests; }

    public void addInteret (Interest interet) { this.interests.add(interet); }

    public void deleteInteret (Interest interet) { this.interests.remove(interet); }*/

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", pseudo='" + pseudo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.uid);
        parcel.writeString(this.pseudo);
        parcel.writeString(this.firstName);
        parcel.writeString(this.lastName);
        parcel.writeString(this.photo);
    }

    public User(Parcel parcel) {
        this.uid = parcel.readLong();
        this.pseudo = parcel.readString();
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.photo = parcel.readString();
    }
}
