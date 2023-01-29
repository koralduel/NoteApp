package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.noteapp.databinding.ActivityNoteListPageBinding;
import com.example.noteapp.databinding.ActivityShowNoteBinding;

public class ShowNote extends AppCompatActivity {

    private ActivityShowNoteBinding binding;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       note = (Note) getIntent().getSerializableExtra("note");

       binding.TvCreationDateValue.setText(note.creationDate);
       binding.TvBodyValue.setText(note.body);
       binding.TvTitleValue.setText(note.title);

        //handling edit button -> go to edit mode
        binding.editBtn.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(),CreateNote.class);
            intent.putExtra("note",note);
            startActivity(intent);
        });

        //handling back button click
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
    }
}