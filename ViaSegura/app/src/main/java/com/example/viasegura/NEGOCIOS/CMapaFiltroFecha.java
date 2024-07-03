package com.example.viasegura.NEGOCIOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.viasegura.DATOS.CBaseDatos;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CMapaFiltroFecha {
    private static final String TAG = "CMapaFiltroFecha";
    public boolean HayDatos(Context Contexto, int idPersona, String fechaInicio, String fechaFin) {
        CBaseDatos Obj1 = new CBaseDatos(Contexto);
        SQLiteDatabase Obj2 = Obj1.getReadableDatabase();

        // Parámetros para la consulta SQL
        String[] Parametros = {String.valueOf(idPersona), fechaInicio, fechaFin};

        // Consulta SQL para verificar si hay datos
        String consultaSQL = "SELECT COUNT(*) " +
                "FROM Denuncia " +
                "INNER JOIN Persona ON Denuncia.id_persona = Persona.idPersona " +
                "WHERE Persona.idPersona = ? " +
                "AND Denuncia.fecha >= ? " +
                "AND Denuncia.fecha <= ?";
        Cursor Filas = Obj2.rawQuery(consultaSQL, Parametros);

        boolean hayDatos = false;
        if (Filas.moveToFirst()) {
            hayDatos = Filas.getInt(0) > 0;
        }

        Filas.close(); // Cerrar el cursor
        Obj1.close(); // Cerrar la base de datos

        return hayDatos;
    }
    public int CargarDatos(Context Contexto, LatLng[] VCoord, String[] VDatos, int idPersona, String fechaInicio, String fechaFin) {
        CBaseDatos Obj1 = new CBaseDatos(Contexto);
        SQLiteDatabase Obj2 = Obj1.getReadableDatabase();

        // Verificar si las fechas se convirtieron correctamente
        Log.d(TAG, "Fecha de inicio SQL: " + fechaInicio);
        Log.d(TAG, "Fecha de fin SQL: " + fechaFin);


        // Parámetros para la consulta SQL
        String[] Parametros = {String.valueOf(idPersona), fechaInicio, fechaFin};

        // Consulta SQL para obtener los datos filtrados por idPersona y rango de fechas
        String consultaSQL = "SELECT Denuncia.latitud, Denuncia.longitud, Categoria.nombre, Persona.nombre, Denuncia.fecha " +
                "FROM Denuncia " +
                "INNER JOIN Categoria ON Denuncia.id_categoria = Categoria.idCategoria " +
                "INNER JOIN Persona ON Denuncia.id_persona = Persona.idPersona " +
                "WHERE Persona.idPersona = ? " +
                "AND Denuncia.fecha >= ? " +
                "AND Denuncia.fecha <= ?";
        Cursor Filas = Obj2.rawQuery(consultaSQL, Parametros);

        // Inicialización de variables
        int NroF = 0;
        int VisDatos = -1;

        // Procesar los resultados del cursor
        if (Filas.moveToFirst()) {
            do {
                VisDatos++;
                // Verificar si latitud y longitud no son nulos
                double latitud = Filas.isNull(0) ? 0 : Filas.getDouble(0);
                double longitud = Filas.isNull(1) ? 0 : Filas.getDouble(1);
                if (latitud != 0 && longitud != 0) {
                    VCoord[VisDatos] = new LatLng(latitud, longitud);
                    VDatos[VisDatos] = Filas.getString(2) + " Denunciante: " + Filas.getString(3) + " Fecha: " + Filas.getString(4);
                    NroF++; // Contar las filas encontradas
                } else {
                    Log.e(TAG, "Latitud o Longitud nulas en fila: " + Filas.getPosition());
                }
            } while (Filas.moveToNext());
        } else {
            Log.d(TAG, "No se encontraron resultados para los parámetros dados.");
        }

        Filas.close(); // Cerrar el cursor
        Obj1.close(); // Cerrar la base de datos

        // Retornar la cantidad de filas encontradas
        return NroF;
    }

    // Método para convertir una fecha en formato "d / M / yyyy" a "yyyy-MM-dd" para SQLite
    private String convertirFechaSQLite(String fecha) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("d / M / yyyy", Locale.getDefault()); // Formato de entrada
            SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Formato para SQLite
            Date date = sdfInput.parse(fecha);
            return sdfOutput.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
