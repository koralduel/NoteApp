package com.example.noteapp;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


//local db -> saving locally on user phone
@Database(entities = {Note.class}, version = 1)
public abstract class LocalDataBase extends RoomDatabase {
    private static LocalDataBase instance;

    public abstract NoteDao noteDao();

    public static LocalDataBase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(NoteApp.context.getApplicationContext(),
                    LocalDataBase.class,
                    NoteApp.context.getString(R.string.LocalDb)).build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
