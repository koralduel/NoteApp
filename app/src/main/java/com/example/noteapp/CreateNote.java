package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivityCreateNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;
import java.util.UUID;


public class CreateNote extends AppCompatActivity implements LocationListener {

    ActivityCreateNoteBinding binding;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    NotesViewModel viewModel = new NotesViewModel();
    LocationManager locationManager;
    LocationListener locationListener;
    String txtLocation;
    Note note;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        note = (Note) getIntent().getSerializableExtra("note");

        if (note == null) {
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String creationDate = dateObj.format(formatter);

            binding.TvCreationDateValue.setText(creationDate);

           /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/

            binding.BtnSave.setOnClickListener(view->{
                String title = binding.ETTitleValue.getText().toString();
                String body = binding.ETBodyValue.getText().toString();
                String uid = UUID.randomUUID().toString();
                String location = txtLocation;
                Note note = new Note(creationDate,title,body,uid,location);

                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)){
                    viewModel.add(note);
                    Toast.makeText(getApplicationContext(), R.string.successfullySaved, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NoteListPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), R.string.errorSave, Toast.LENGTH_LONG).show();
                }
            });

            binding.BtnDelete.setOnClickListener(view -> {
                finish();
            });
        }
        else{
            binding.BtnSave.setOnClickListener(view -> {
                String creationDate = note.getCreationDate();
                String uid = note.getUid();
                String location = note.getLocation();
                String title = binding.ETTitleValue.getText().toString();
                String body = binding.ETBodyValue.getText().toString();
                Note note = new Note(creationDate,title,body,uid,location);

                viewModel.updateNote(note);
                Toast.makeText(getApplicationContext(), R.string.successfullySaved, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NoteListPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
            binding.BtnDelete.setOnClickListener(view ->{
                viewModel.delete(note);
            });

        }

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });



    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
         txtLocation = "latitude"+ location.getLatitude() + ","+"longitude" + location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}