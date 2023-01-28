package com.example.noteapp;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class NotesRepository {

    private Firebase fireBase;
    private static  NotesRepository notesRepository = new NotesRepository();
   // private noteListData noteListData;


    private NotesRepository() {
        //noteListData = new noteListData();
        fireBase = new Firebase();
    }

    /*public class noteListData extends MutableLiveData<List<Note>> {

        public noteListData() {
            super();
            setValue(new LinkedList<>());
        }


    }*/

    //buid a firebase class and execute these functions
    public List<Note> getAllNotes(){
        return fireBase.getAllNotes();
    }

   /* public LiveData<List<Note>> getAll() {
        return noteListData;
    }*/

    public void add (final Note note) {
        fireBase.add(note);
    }

    public void delete (final Note note) {
        fireBase.delete(note);
    }

    public void updateNote(final Note note) {fireBase.updateNote(note);}

    public static NotesRepository getNotesRepository() {
        return notesRepository;
    }
}
