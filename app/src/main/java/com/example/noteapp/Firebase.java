package com.example.noteapp;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

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

    private static StorageReference storageRef;
    //private FirebaseDatabase dateBase = FirebaseDatabase.getInstance();


    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Notes").child(user.getUid());

    public Firebase(NotesRepository.NoteListData noteListData) {
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Note> notes = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Note note = dataSnapshot.getValue(Note.class);
                    notes.add(note);
                }
                noteListData.setValue(notes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void add(Note note) {
        //storageRef = FirebaseStorage.getInstance().getReference();
        dataRef.child(String.valueOf(note.getUid())).setValue(note);

    }

    public void delete(Note note){

        dataRef.child(note.getUid()).removeValue();
        //storageRef=FirebaseStorage.getInstance().getReference();
        //storageRef.child("Posts").child(post.getId()).delete();
    }

    public void updateNote(Note note){
        //dataRef.child(note.getUid()).updateChildren(note);
    }


}
