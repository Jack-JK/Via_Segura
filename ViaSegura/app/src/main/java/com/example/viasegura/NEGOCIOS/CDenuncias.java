package com.example.viasegura.NEGOCIOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viasegura.DATOS.CBaseDatos;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CDenuncias {

    ArrayAdapter<String> Adaptador;

    public void CargarCategoria(Context contexto, Spinner cat) {
        CBaseDatos obj1 = new CBaseDatos(contexto);
        SQLiteDatabase obj2 = obj1.getReadableDatabase();
        Cursor filas = obj2.rawQuery("SELECT nombre FROM Categoria ORDER BY CASE WHEN nombre = 'Otro' THEN 1 ELSE 0 END, nombre ASC", null);

        if (filas != null) {
            int nroF = filas.getCount();
            if (nroF > 0) {
                String[] datos = new String[nroF];
                int visDatos = -1;
                if (filas.moveToFirst()) {
                    do {
                        visDatos++;
                        datos[visDatos] = filas.getString(0);
                    } while (filas.moveToNext());
                }
                Adaptador = new ArrayAdapter<>(contexto, android.R.layout.simple_list_item_1, datos);
                cat.setAdapter(Adaptador);
                System.out.println("Datos cargados en el Spinner.");
            } else {
                System.out.println("No se encontraron categorías en la base de datos.");
            }
            filas.close();
        } else {
            System.out.println("El cursor es nulo.");
        }
        obj1.close();
    }

    public String obtenerNombreCategoria(Context contexto, int idCategoria) {
        String nombreCategoria = "";
        CBaseDatos dbHelper = new CBaseDatos(contexto);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM Categoria WHERE idCategoria = ?", new String[]{String.valueOf(idCategoria)});

        if (cursor != null && cursor.moveToFirst()) {
            nombreCategoria = cursor.getString(0);
            cursor.close();
        }

        db.close();
        dbHelper.close();

        return nombreCategoria;
    }

    public void insertarDenuncia(Context contexto, String imagePath, double latitud, double longitud, String fecha, String hora, String descripcion, int idPersona, int idCategoria, int idEstado) {
        CBaseDatos dbHelper = new CBaseDatos(contexto);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("foto", imagePath);
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        values.put("fecha", fecha);
        values.put("hora", hora); // nuevo atributo
        values.put("descripcion", descripcion);
        values.put("id_persona", idPersona);
        values.put("id_categoria", idCategoria);
        values.put("id_estado", idEstado);

        long result = db.insert("Denuncia", null, values); // Insertar y obtener el resultado

        if (result == -1) {
            // Error al insertar
            Toast.makeText(contexto, "Error al guardar la denuncia", Toast.LENGTH_SHORT).show();
        } else {
            // Éxito al insertar
            Toast.makeText(contexto, "Denuncia guardada correctamente", Toast.LENGTH_SHORT).show();
        }

        db.close();
        dbHelper.close();
    }

    public int getIdCategoriaPorNombre(Context contexto, String nombreCategoria) {
        CBaseDatos obj1 = new CBaseDatos(contexto);
        SQLiteDatabase obj2 = obj1.getReadableDatabase();
        String[] args = new String[]{nombreCategoria};
        Cursor filas = obj2.rawQuery("SELECT idCategoria FROM Categoria WHERE nombre = ?", args);
        int idCategoria = -1;
        if (filas.moveToFirst()) {
            idCategoria = filas.getInt(0); // Obtener el idCategoria del primer registro
        }
        filas.close();
        obj1.close();
        return idCategoria;
    }


    // Método para mostrar las denuncias en un GridView solo para la persona logeada
    public void Mostrar(Context contexto, GridView gv, int idPersona) {
        CBaseDatos dbHelper = new CBaseDatos(contexto);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT D.idDenuncia, D.fecha, D.hora, D.foto, D.latitud, D.longitud, D.descripcion, C.nombre AS nombre_categoria " +
                "FROM Denuncia D " +
                "INNER JOIN Categoria C ON D.id_categoria = C.idCategoria " +
                "WHERE D.id_persona = ?"; // Filtrar por id_persona

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idPersona)});

        // Crear y configurar el adaptador personalizado
        DenunciaAdapter adapter = new DenunciaAdapter(contexto, cursor);
        gv.setAdapter(adapter);
    }

    public void CargarDenunciasPorCategoria(Context context, Spinner spinner, int idPersona, int idCategoria) {
        CBaseDatos dbHelper = new CBaseDatos(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<String> denuncias = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT idDenuncia FROM Denuncia WHERE id_persona = ? AND id_categoria = ?",
                new String[]{String.valueOf(idPersona), String.valueOf(idCategoria)});

        if (cursor.moveToFirst()) {
            do {
                denuncias.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, denuncias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void CargarCategoria2(Context context, Spinner spinner) {
        CBaseDatos dbHelper = new CBaseDatos(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<String> categorias = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT idCategoria, nombre FROM Categoria", null);

        if (cursor.moveToFirst()) {
            do {
                categorias.add(cursor.getString(1)); // Asumiendo que el nombre de la categoría está en la segunda columna
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void obtenerDetallesDenuncia(Context context, int idDenuncia, ImageView imgFoto, TextView hora, TextView fecha, TextView descripcion, double[] latLong) {
        CBaseDatos dbHelper = new CBaseDatos(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT foto, latitud, longitud, fecha, hora, descripcion FROM Denuncia WHERE idDenuncia = ?", new String[]{String.valueOf(idDenuncia)});
        if (cursor.moveToFirst()) {
            String fotoPath = cursor.getString(cursor.getColumnIndexOrThrow("foto"));
            String denunciaHora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
            String denunciaFecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
            String denunciaDescripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
            double latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"));
            double longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"));

            // Cargar imagen
            File imgFile = new File(fotoPath);
            if (imgFile.exists()) {
                Picasso.get().load(imgFile).into(imgFoto);
            } else {
                Log.e("ImagenError", "El archivo de imagen no existe en la ruta: " + fotoPath);
            }

            hora.setText(denunciaHora);
            fecha.setText(denunciaFecha);
            descripcion.setText(denunciaDescripcion);
            latLong[0] = latitud;
            latLong[1] = longitud;
        }
        cursor.close();
        db.close();
    }


}
