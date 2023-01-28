package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.noteapp.databinding.ActivityNoteListPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        viewModel= new ViewModelProvider(this).get(NotesViewModel.class);

        notes = new ArrayList<>();

        viewModel.get().observe(this,p->{
            notes.clear();
            notes.addAll(p);
            adapter.setPosts(p);
           // binding.RVNotesList.setRefreshing(false);
        });

        adapter = new Adapter_Note(notes,this);
        binding.RVNotesList.setLayoutManager(new LinearLayoutManager(this));
        binding.RVNotesList.setAdapter(adapter);


        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if(item.getTitle().equals(getString(R.string.viewMode))){
                Intent intent = new Intent(this,MapNotePage.class);
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
        Intent intent = new Intent(this,ShowNote.class);
        intent.putExtra("note",notes.get(position));
        startActivity(intent);
    }
}