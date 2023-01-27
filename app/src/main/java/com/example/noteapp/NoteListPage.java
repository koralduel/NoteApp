package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.databinding.ActivityNoteListPageBinding;
import com.example.noteapp.databinding.ActivityRegisterPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteListPage extends AppCompatActivity {

    private ActivityNoteListPageBinding binding;

    //FirebaseUser user;
    FirebaseAuth firebaseAuth;
    Intent intent;
    DatabaseReference reference;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        //intent from registration or login page-> get logged userID
        intent= getIntent();
        String email=intent.getStringExtra("email");

        reference = FirebaseDatabase.getInstance().getReference("Users");

        binding.topAppBar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getTitle().equals(getString(R.string.viewMode))){
                Intent intent = new Intent(this,NoteMapPage.class);
                startActivity(intent);
            }
            if(menuItem.getTitle().equals(getString(R.string.addNote))){
                Intent intent = new Intent(this,CreateNote.class);
                startActivity(intent);
            }
            if(menuItem.getTitle().equals(getString(R.string.logout))){
                firebaseAuth.signOut();
                startActivity(new Intent(this, LoginPage.class));
            }
            return true;
        });





        //binding.topAppBar.setTitle("Welcome,"+ currentUser.getName());
    }
}