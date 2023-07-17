package com.grupo5.pm2e1grupo5;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grupo5.pm2e1grupo5.config.Contactos;
import com.grupo5.pm2e1grupo5.databinding.ActivityMaps2Binding;

public class Maps_Activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    Contactos element;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        element = (Contactos) getIntent().getSerializableExtra("contact");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng( Double.parseDouble(element.getLatitud()), Double.parseDouble(element.getLogintud()));
        mMap.addMarker(new MarkerOptions().position(sydney).title(element.getNombres()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }
}