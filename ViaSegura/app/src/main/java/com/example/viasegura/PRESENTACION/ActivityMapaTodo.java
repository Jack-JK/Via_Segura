package com.example.viasegura.PRESENTACION;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.viasegura.NEGOCIOS.CMapaFiltroFecha;
import com.example.viasegura.NEGOCIOS.CMapaTodo;
import com.example.viasegura.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.viasegura.databinding.ActivityMapaTodoBinding;

public class ActivityMapaTodo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaTodoBinding binding;
    Button BtnSat, BtnNor;
    CMapaTodo OjbTodo = new CMapaTodo();
    //variables para llenar de daos las marcas en el mapa
    LatLng[] VCoord = new LatLng[1000];
    String[] VDatos = new String[1000];
    int Numero;
    private int idPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnSat = findViewById(R.id.BtnSatelite);
        BtnNor = findViewById(R.id.BtnNormal);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("idPersona")) {
            idPersona = intent.getIntExtra("idPersona", -1);}

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //obtenemos los datos de la activityCoordenadas que preparamos en los vectores
        Numero = OjbTodo.CargarDatos(this, VCoord, VDatos, idPersona);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        //habilitamos el zoom dentro del mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);

        for (int V= 0; V < Numero;V++)
        {
            MarkerOptions Marca = new MarkerOptions();
            Marca.position(VCoord[V]);
            Marca.title(VDatos[V]);
            Marca.snippet(VCoord[V].toString());
            // Utilizar el icono por defecto para todos los marcadores
            Marca.icon(BitmapDescriptorFactory.defaultMarker());
            mMap.addMarker(Marca);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(VCoord[0]));
        //prepara y realiza un zoom en la ubicacion
        CameraPosition Camara = new CameraPosition.Builder().target(VCoord[0]).zoom(10).bearing(90).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(Camara));
    }
}