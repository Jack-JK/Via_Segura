package com.example.viasegura.NEGOCIOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.viasegura.DATOS.CBaseDatos;

public class CLogin {
    public int getIdPersona(Context contexto, String usu) {
        CBaseDatos obj1 = new CBaseDatos(contexto);
        SQLiteDatabase obj2 = obj1.getReadableDatabase();
        String[] args = new String[]{usu};
        Cursor filas = obj2.rawQuery("SELECT id_persona FROM Usuario WHERE correo = ?", args);
        int idPersona = -1;
        if (filas.moveToFirst()) {
            idPersona = filas.getInt(0); // Obtener el idPersona del primer registro
        }
        filas.close();
        obj1.close();
        return idPersona;
    }
    public boolean ValidarDatos (Context Contexto, String usu, String cont)
    {
        CBaseDatos obj1 = new CBaseDatos(Contexto);
        SQLiteDatabase obj2 = obj1.getReadableDatabase();
        String[] args = new String[]{usu, cont};
        Cursor filas = obj2.rawQuery("SELECT id_persona FROM Usuario WHERE correo = ? AND contrasena = ?", args);
        boolean ses = filas.getCount() > 0;
        filas.close();
        obj1.close();
        return ses;
    }

    public int getIdPersonaD(Context context, String usuario) {
        int idPersona = -1; // Valor por defecto si no se encuentra el usuario

        // Aquí deberías consultar tu base de datos para obtener el idPersona
        CBaseDatos base = new CBaseDatos(context);
        SQLiteDatabase db = base.getReadableDatabase();

        String[] parametros = {usuario};
        String[] campos = {"idPersona"};

        try {
            Cursor cursor = db.query("Persona", campos, "usuario=?", parametros, null, null, null);
            cursor.moveToFirst();
            idPersona = cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(context, "Error al buscar idPersona: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }

        return idPersona;
    }

}
