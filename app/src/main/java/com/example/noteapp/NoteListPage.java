package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.noteapp.databinding.ActivityNoteListPageBinding;
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
    DatabaseReference reference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users");


        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if(item.getTitle().equals(getString(R.string.viewMode))){
                Intent intent = new Intent(this,NoteMapPage.class);
                startActivity(intent);
            }
            else if(item.getTitle().equals(getString(R.string.logout))){
                firebaseAuth.signOut();
                startActivity(new Intent(this, LoginPage.class));
            }
            return true;

        });

        binding.fab.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(),CreateNote.class);
            startActivity(intent);
        });


        binding.TvWelcome.setText("Welcome,"+ currentUser.getDisplayName());
    }
}