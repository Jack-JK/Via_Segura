package com.example.viasegura.PRESENTACION;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.viasegura.NEGOCIOS.CDenuncias;
import com.example.viasegura.R;

public class MostrarDenunciasActivity extends AppCompatActivity {

    private GridView gridView;
    private CDenuncias cDenuncias;
    private int idPersona; // Variable para almacenar el idPersona obtenido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_denuncias);

        gridView = findViewById(R.id.gridview_denuncias);
        cDenuncias = new CDenuncias();

        try {
            // Obtener el idPersona pasado desde ActivityLogin o ActivityInicio
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                idPersona = extras.getInt("idPersona");
            } else {
                // Manejar el caso donde no se recibe el idPersona correctamente
                Log.e("MostrarDenunciasActivity", "No se recibió el idPersona correctamente");
                finish(); // Finaliza la actividad si no hay idPersona disponible
            }


            // Llamar al método Mostrar de CDenuncias para cargar los datos en el GridView solo para la persona logeada
            cDenuncias.Mostrar(this, gridView, idPersona);

            // Configurar el botón Regresar
            Button btnRegresar = findViewById(R.id.boton_regresar);
            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Finaliza esta actividad y regresa a la actividad anterior
                }
            });
        } catch (Exception e) {
            // Captura cualquier excepción que pueda ocurrir durante la inicialización de la actividad
            Log.e("MostrarDenunciasActivity", "Error en onCreate: " + e.getMessage());
            e.printStackTrace();
            // Puedes mostrar un mensaje al usuario o realizar otra acción según sea necesario
            finish(); // Finaliza la actividad en caso de error
        }
    }
}
