package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
public class Note implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    String uid;
    @ColumnInfo
    String creationDate;
    @ColumnInfo
    String title;
    @ColumnInfo
    String body;
    @ColumnInfo
    String location;
    @ColumnInfo
    String userUid;

    public Note() { }

    public Note(Note note){
        this.location = note.getLocation();
        this.body = note.getBody();
        this.uid = note.getUid();
        this.title = note.getTitle();
        this.creationDate = note.getCreationDate();
    }

    public Note(String creationDate, String title, String body, String uid, String location,String userUid) {
        this.creationDate = creationDate;
        this.title = title;
        this.body = body;
        this.uid = uid;
        this.location = location;
        this.userUid = userUid;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
