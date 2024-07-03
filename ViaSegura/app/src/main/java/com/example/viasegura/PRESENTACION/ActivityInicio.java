package com.example.viasegura.PRESENTACION;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.viasegura.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityInicio extends AppCompatActivity {
    FragmentInicio inicio = new FragmentInicio();
    FragmentDenunciar denunciar = new FragmentDenunciar();
    FragmentMisDenuncias misdenuncias = new FragmentMisDenuncias();
    FragmentFiltros filtros = new FragmentFiltros();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // Configurar la navegación inferior
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(item -> onNavigationItemSelected(item));

        // Cargar el fragmento de inicio por defecto
        loadFragment(inicio);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        int itemId = menuItem.getItemId();

        if (itemId == R.id.inicio) {
            selectedFragment = inicio;
        } else if (itemId == R.id.denunciar) {
            selectedFragment = denunciar;
        } else if (itemId == R.id.misdenuncias) {
            selectedFragment = misdenuncias;
        } else if (itemId == R.id.filtros) {
            selectedFragment = filtros;
        }

        if (selectedFragment != null) {
            // Pasar idPersona al fragmento seleccionado
            Bundle bundle = new Bundle();
            bundle.putInt("idPersona", getIntent().getIntExtra("idPersona", -1));
            selectedFragment.setArguments(bundle);

            loadFragment(selectedFragment);
            return true;
        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.commit();
    }
}
