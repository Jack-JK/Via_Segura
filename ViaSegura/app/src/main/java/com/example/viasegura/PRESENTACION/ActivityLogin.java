package com.example.viasegura.PRESENTACION;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.viasegura.NEGOCIOS.CLogin;
import com.example.viasegura.R;

public class ActivityLogin extends AppCompatActivity {
    EditText usu, cont;
    CLogin ObjLog = new CLogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usu = findViewById(R.id.textUsuario);
        cont = findViewById(R.id.textContraseña);
    }

    public void Validar(View Vista) {
        String usuario = usu.getText().toString();
        String contrasena = cont.getText().toString();

        if (ObjLog.ValidarDatos(this, usuario, contrasena)) {
            int idPersona = ObjLog.getIdPersona(this, usuario);
            GoHome(idPersona);
        } else {
            Toast.makeText(this, "Error al ingresar los datos, verifique...", Toast.LENGTH_SHORT).show();
        }
    }

    public void GoHome(int idPersona) {
        Intent obj = new Intent(this, ActivityInicio.class);
        obj.putExtra("idPersona", idPersona); // Pasar idPersona a ActivityInicio
        startActivity(obj);
    }

    public void GoRegister(View vista) {
        Intent obj = new Intent(this, ActivityRegistro.class);
        startActivity(obj);
    }
}