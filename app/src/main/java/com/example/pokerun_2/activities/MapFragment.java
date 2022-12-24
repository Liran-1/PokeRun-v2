package com.example.pokerun_2.activities;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.example.pokerun_2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.score_FRG_map);
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng defaultLocation = new LatLng(0, 0);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // Add a marker on the map coordinates.
        googleMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .title("Default"));


//        // Move the camera to the map coordinates and zoom in closer.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
//        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }


}
