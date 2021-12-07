package com.uqac.proximty.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"name"},
        unique = true)})
public class Interest {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    public Interest(){

    }

    public Interest(String name){
        this.name=name;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
