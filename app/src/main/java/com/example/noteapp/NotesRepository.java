package com.example.noteapp;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

public class NotesRepository {

    private NoteDao dao;
    private Firebase fireBase;
    private NoteListData noteListData;
    private static  NotesRepository notesRepository = new NotesRepository();


    private NotesRepository() {
        LocalDataBase db = LocalDataBase.getInstance();
        dao = db.noteDao();
        noteListData = new NoteListData();
        fireBase = new Firebase(dao,noteListData);
    }

    public class NoteListData extends MutableLiveData<List<Note>> {
        public NoteListData() {
            super();
            setValue(new LinkedList<>());
        }
        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                noteListData.postValue(dao.get());
            }).start();
        }

    }



    public LiveData<List<Note>> getAll() {
        return noteListData;
    }

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
