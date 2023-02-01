package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.noteapp.databinding.ActivityMapNotePageBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapNotePage extends AppCompatActivity {

    MapView mapView;
    FirebaseAuth firebaseAuth;


    private ActivityMapNotePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapNotePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        mapView = binding.mapView;

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();

        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("notes app", Context.MODE_PRIVATE));

        //handling bottom navigation clicks
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if(item.getTitle().equals(getString(R.string.viewMode))){
                Intent intent = new Intent(this,NoteListPage.class);
                startActivity(intent);
            }
            else if(item.getTitle().equals(getString(R.string.logout))){
                firebaseAuth.signOut();
                startActivity(new Intent(this, LoginPage.class));
            }
            return true;

        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MapNotePage.this);
        builder.setMessage(R.string.MapAlert);
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        List<Marker> markers = new ArrayList<>();


        //get all user notes form db*/

        List<Note> notes = new ArrayList<>();
        NotesViewModel viewModel = new NotesViewModel();
        notes.addAll(viewModel.get().getValue());

        List<Note> myNotes = new ArrayList<>();
        for(Note n:notes){
            if(n.getUserUid().equals(currentUser.getUid())){
                myNotes.add(n);
            }
        }

        

        //pins all user notes on the map
        for (Note note : myNotes) {
            if(note.getLocation() !=null){
                double latitude = getlatitude(note);
                double longitude = getlongitude(note);
                GeoPoint point = new GeoPoint(latitude, longitude);

                Marker marker = new Marker(mapView);
                marker.setPosition(point);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setTitle(note.getTitle());
                marker.setSnippet(note.getBody());

                markers.add(marker);

                mapView.getOverlays().add(marker);

                //when Clicking on a note - goes to show note page
                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        // Open the note in a new activity or fragment
                        Intent intent = new Intent(MapNotePage.this, ShowNote.class);
                        intent.putExtra("note", note);
                        startActivity(intent);
                        return true;
                    }
                });
            }

        }


        mapController.setZoom(3.5);
        if (myNotes.size() > 0) {
            for (Note note:myNotes) {
                if(note.getLocation()!=null)
                    mapController.setCenter(markers.get(0).getPosition());
            }

        }





    }
    //return only latitude value from location string in note
    public Float getlatitude(Note note){
        String[] values = note.getLocation().split(",");
        String latitude = values[0].split(":")[1];
        return Float.parseFloat(latitude);
    }

    //return only longitude value from location string in note
    public Float getlongitude(Note note){
        String[] values = note.getLocation().split(",");
        String longitude = values[1].split(":")[1];
        return Float.parseFloat(longitude);
    }



}