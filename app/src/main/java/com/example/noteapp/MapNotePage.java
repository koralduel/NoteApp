package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;


import com.example.noteapp.databinding.ActivityMapNotePageBinding;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapNotePage extends AppCompatActivity {
    MapView mapView;
    private ActivityMapNotePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapNotePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("notes app", Context.MODE_PRIVATE));


        //get all user notes form db
        List<Note> notes = new ArrayList<>();
        List<Marker> markers = new ArrayList<>();

        notes.add(new Note("12-16-22", "koko", "koko", "1234", "latitude:37.422065599999996,longitude:-122.08408969999998"));

        //pins all user notes on the map
        for (Note note : notes) {
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


        Marker firstMarker = markers.get(0);
        GeoPoint firstMarkerPos = firstMarker.getPosition();

        IMapController mapController = mapView.getController();
        mapController.setZoom(3.5);
        mapController.setCenter(firstMarkerPos);

    }

    public Float getlatitude(Note note){
        String[] values = note.getLocation().split(",");
        String latitude = values[0].split(":")[1];
        return Float.parseFloat(latitude);
    }

    public Float getlongitude(Note note){
        String[] values = note.getLocation().split(",");
        String longitude = values[1].split(":")[1];
        return Float.parseFloat(longitude);
    }



}