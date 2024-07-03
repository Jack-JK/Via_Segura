package com.example.viasegura.PRESENTACION;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.viasegura.NEGOCIOS.CPersona;
import com.example.viasegura.NEGOCIOS.CUsuario;
import com.example.viasegura.R;

public class ActivityRegistro extends AppCompatActivity {
    EditText Nom, Ape, Ced, Cor, Tel, Cont;
    int TelG;
    String NomG, ApeG, CedG, CorG, ContG;
    CPersona ObjPer = new CPersona();
    CUsuario ObjUsu = new CUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Inicializar los EditText
            Nom = findViewById(R.id.editTextNombre);
            Ape = findViewById(R.id.editTextApellido);
            Ced = findViewById(R.id.editTextCedula);
            Cor = findViewById(R.id.editTextCorreo);
            Tel = findViewById(R.id.editTextTelefono);
            Cont = findViewById(R.id.editTextContrasena);

            return insets;
        });
    }//FIN ONCREATE
    public void GuardarRegistro (View Vista)
    {
        // Obtener los valores de los EditText
        String nombre = Nom.getText().toString();
        String apellido = Ape.getText().toString();
        String cedula = Ced.getText().toString();
        String telefono = Tel.getText().toString();
        String correo = Cor.getText().toString();
        String contraseña = Cont.getText().toString();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || telefono.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            ObjPer.GuardarPer(this, nombre, apellido, cedula, Integer.parseInt(telefono));
            ObjUsu.GuardarUsu(this, correo, contraseña);
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            GoMain();
        }
    }
    public void GoMain()
    {
        Intent obj = new Intent(this, MainActivity.class);
        startActivity(obj);
    }
}