package com.example.noteapp;

import android.app.Application;
import android.content.Context;

public class NoteApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
