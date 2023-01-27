package com.example.noteapp;

import java.time.LocalDateTime;


public class Note {

    String creationDate;
    String title;
    String body;
    String uid;
    String location;

    public Note() { }

    public Note(String creationDate, String title, String body, String uid, String location) {
        this.creationDate = creationDate;
        this.title = title;
        this.body = body;
        this.uid = uid;
        this.location = location;
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
}
