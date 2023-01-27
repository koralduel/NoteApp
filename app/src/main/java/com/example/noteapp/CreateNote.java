package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class CreateNote extends AppCompatActivity {

    ActivityCreateNoteBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String creationDate = dateObj.format(formatter);

        binding.TvCreationDateValue.setText(creationDate);

        binding.BtnSave.setOnClickListener(view->{

            String title = binding.ETTitleValue.getText().toString();
            String body = binding.ETBodyValue.getText().toString();
            String uid = UUID.randomUUID().toString();
            String location = "location";
            Note note = new Note(creationDate,title,body,uid,location);

            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)){
                databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(currentUser.getUid()).child(uid);

                databaseReference.setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.successfullySaved, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), NoteListPage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });

            }else {
                Toast.makeText(getApplicationContext(), R.string.errorSave, Toast.LENGTH_LONG).show();
            }
        });
    }
}