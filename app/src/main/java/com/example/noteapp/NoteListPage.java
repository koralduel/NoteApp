package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.databinding.ActivityNoteListPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteListPage extends AppCompatActivity implements ClickInterface{

    private ActivityNoteListPageBinding binding;

    //FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    FirebaseUser currentUser;
    NotesViewModel viewModel;

    Adapter_Note adapter;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        viewModel= new ViewModelProvider(this).get(NotesViewModel.class);

        notes = new ArrayList<>();
        binding.RVNotesList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Note(notes,this);
        binding.RVNotesList.setAdapter(adapter);


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

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this,CreateNote.class);
        //intent.putExtra("mail",notes.get(position));
        startActivity(intent);
    }
}