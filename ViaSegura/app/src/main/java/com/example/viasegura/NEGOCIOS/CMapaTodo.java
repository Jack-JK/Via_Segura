package com.example.viasegura.NEGOCIOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.viasegura.DATOS.CBaseDatos;
import com.google.android.gms.maps.model.LatLng;

public class CMapaTodo {
    private static final String TAG = "CMapaTodo"; // Etiqueta para logs

    public boolean HayDatos(Context contexto, int idPersona) {
        CBaseDatos baseDatos = new CBaseDatos(contexto);
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Consulta SQL para verificar si hay datos de denuncias para el idPersona
        String consultaSQL = "SELECT COUNT(*) " +
                "FROM Denuncia " +
                "WHERE id_persona = ?";

        // Parámetros para la consulta SQL
        String[] parametros = { String.valueOf(idPersona) };

        Cursor cursor = db.rawQuery(consultaSQL, parametros);

        boolean hayDatos = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                hayDatos = cursor.getInt(0) > 0;
            }
            cursor.close(); // Cerrar el cursor
        }

        baseDatos.close(); // Cerrar la base de datos

        return hayDatos;
    }
    public int CargarDatos(Context contexto, LatLng[] VCoord, String[] VDatos, int idPersona) {
        CBaseDatos baseDatos = new CBaseDatos(contexto);
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Consulta SQL para obtener los datos filtrados por idPersona
        String consultaSQL = "SELECT Denuncia.latitud, Denuncia.longitud, Categoria.nombre, Persona.nombre, Denuncia.fecha " +
                "FROM Denuncia " +
                "INNER JOIN Categoria ON Denuncia.id_categoria = Categoria.idCategoria " +
                "INNER JOIN Persona ON Denuncia.id_persona = Persona.idPersona " +
                "WHERE Persona.idPersona = ? ";

        // Parámetros para la consulta SQL
        String[] parametros = { String.valueOf(idPersona) };

        Cursor cursor = db.rawQuery(consultaSQL, parametros);

        // Inicialización de variables
        int numeroFilas = 0;
        int indice = -1;

        // Procesar los resultados del cursor
        if (cursor != null) {
            while (cursor.moveToNext()) {
                indice++;
                // Verificar si latitud y longitud no son nulos
                double latitud = cursor.isNull(0) ? 0 : cursor.getDouble(0);
                double longitud = cursor.isNull(1) ? 0 : cursor.getDouble(1);
                if (latitud != 0 && longitud != 0) {
                    VCoord[indice] = new LatLng(latitud, longitud);
                    VDatos[indice] = cursor.getString(2) + " Denunciante: " + cursor.getString(3) + " Fecha: " + cursor.getString(4);
                    numeroFilas++; // Contar las filas encontradas
                } else {
                    Log.e(TAG, "Latitud o Longitud nulas en fila: " + cursor.getPosition());
                }
            }
            cursor.close(); // Cerrar el cursor
        } else {
            Log.d(TAG, "Cursor es nulo.");
        }

        baseDatos.close(); // Cerrar la base de datos

        // Retornar la cantidad de filas encontradas
        return numeroFilas;
    }
}
