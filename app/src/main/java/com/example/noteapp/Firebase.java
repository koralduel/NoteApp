package com.example.noteapp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Firebase {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private NoteDao dao;
    private NotesRepository.NoteListData noteListData;
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Notes");

    //get all user notes from db -> put it the the dao
    public Firebase(NoteDao dao,NotesRepository.NoteListData noteListData)
    {
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> notes = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Note note = dataSnapshot.getValue(Note.class);
                    notes.add(note);
                }
                noteListData.setValue(notes);
                new Thread(()->{
                    dao.clear();
                    dao.insertList(notes);
                }).start();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error","Failed to change data in dao");
            }
        });
        this.dao=dao;
        this.noteListData=noteListData;
    }

    //add new note to db
    public void add(Note note) {
        dataRef.child(String.valueOf(note.getUid())).setValue(note);

    }

    //delete note from db
    public void delete(Note note){

        dataRef.child(note.getUid()).removeValue();
    }

    //update an existing note in db
    public void updateNote(Note note){
        HashMap<String, Object> newNote = new HashMap<>();
        newNote.put("body",note.getBody());
        newNote.put("creationDate",note.getCreationDate());
        newNote.put("location",note.getLocation());
        newNote.put("title",note.getTitle());
        newNote.put("uid",note.getUid());
        dataRef.child(note.getUid()).updateChildren(newNote);
    }




}
