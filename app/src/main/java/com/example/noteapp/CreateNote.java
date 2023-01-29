package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.Locale;
import java.util.UUID;


public class CreateNote extends AppCompatActivity implements LocationListener {

    ActivityCreateNoteBinding binding;
    FirebaseAuth mAuth;
    NotesViewModel viewModel = new NotesViewModel();
    LocationManager locationManager;
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

        //get not from intent(came from showNote page)
        note = (Note) getIntent().getSerializableExtra("note");

        //new note(not from showNote page)
        if (note == null) {

            //date when note created
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String creationDate = dateObj.format(formatter);

            binding.TvCreationDateValue.setText(creationDate);

            //check if there are permission to get location -> if not ask
            if(ContextCompat.checkSelfPermission(CreateNote.this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CreateNote.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
            }
            //saving new note to db
            binding.BtnSave.setOnClickListener(view->{
                String title = binding.ETTitleValue.getText().toString();
                String body = binding.ETBodyValue.getText().toString();
                String uid = UUID.randomUUID().toString();
                getLocation();
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
            //when clicking delete on new note(not save to db yet)
            binding.BtnDelete.setOnClickListener(view -> {
                finish();
            });
        }
        else{
            //editing note-> came from show note
            binding.TvCreationDateValue.setText(note.getCreationDate());
            binding.ETTitleValue.setText(note.getTitle());
            binding.ETBodyValue.setText(note.getBody());

            //when saving -> update the existing note
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
            //when deleting -> delete from db
            binding.BtnDelete.setOnClickListener(view ->{
                viewModel.delete(note);
                Toast.makeText(getApplicationContext(), R.string.successfullyDeleted, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NoteListPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });

        }
        //back to page user been before
        binding.backBtn.setOnClickListener(view -> {
            finish();
        });



    }

    //save in txtLocation the concat of latitude and longitude points of the user location
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,CreateNote.this);
            Geocoder geocoder = new Geocoder(CreateNote.this, Locale.getDefault());
            Location currentLocation =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
            String add = addresses.get(0).getAddressLine(0);

            List<Address> addresses2 = geocoder.getFromLocationName(add, 1);
            if(addresses.size() > 0) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                txtLocation = "latitude:" + String.valueOf(latitude) +","+ "longitude:" + String.valueOf(longitude);
            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try{
            Geocoder geocoder = new Geocoder(CreateNote.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            txtLocation = addresses.get(0).getAddressLine(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
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