package com.example.viasegura.PRESENTACION;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.viasegura.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaFragmentMisDenuncias extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapafragment_mis_denuncias);

        latitud = getIntent().getDoubleExtra("latitud", -17.7833);
        longitud = getIntent().getDoubleExtra("longitud", -63.1821);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finaliza la actividad actual y vuelve a la anterior
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(location).title("Ubicación de la denuncia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15).bearing(90).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
