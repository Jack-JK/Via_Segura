package com.example.viasegura.NEGOCIOS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.viasegura.PRESENTACION.FragmentDenunciar;
import com.example.viasegura.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng selectedLocation;
    Button BtnSat, BtnNor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cmapa);

        BtnSat = findViewById(R.id.btnSatelital);
        BtnNor = findViewById(R.id.btnNormal);

        //enlaces
        BtnSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
            }
        });
        BtnNor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.btnGuardar).setOnClickListener(view -> {
            if (selectedLocation != null) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latitud", selectedLocation.latitude);
                returnIntent.putExtra("longitud", selectedLocation.longitude);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //le agrega el zoom al emulador
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mueve la camara a la coordenada
        LatLng santaCruz = new LatLng(-17.7833, -63.1821);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 12));
        //prepara y realiza un zoom en la ubicacion
        CameraPosition Camara = new CameraPosition.Builder().target(santaCruz).zoom(10).bearing(90).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(Camara));

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            selectedLocation = latLng;
        });
    }
}
