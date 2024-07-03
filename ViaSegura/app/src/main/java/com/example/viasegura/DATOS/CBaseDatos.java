package com.example.viasegura.DATOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CBaseDatos extends SQLiteOpenHelper {
    private static final String name = "BDviasegura";
    private static final int version = 6;

    public CBaseDatos(@Nullable Context context)
    {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla Persona
        db.execSQL("CREATE TABLE Persona (" +
                "idPersona INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "apellido TEXT NOT NULL, " +
                "cedula TEXT NOT NULL, " +
                "telefono INTEGER NOT NULL, " +
                "fecha DATE NOT NULL)");

        // Tabla Usuario
        db.execSQL("CREATE TABLE Usuario (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "correo TEXT NOT NULL, " +
                "contrasena TEXT NOT NULL, " +
                "id_persona INTEGER NOT NULL, " +
                "FOREIGN KEY (id_persona) REFERENCES Persona(idPersona))");

        // Tabla Denuncia
        db.execSQL("CREATE TABLE Denuncia (" +
                "idDenuncia INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "foto TEXT, " +
                "latitud DOUBLE NOT NULL, " +
                "longitud DOUBLE NOT NULL, " +
                "fecha DATE NOT NULL, " +
                "hora TIME NOT NULL, " +
                "descripcion TEXT NOT NULL, " +
                "id_persona INTEGER NOT NULL, " +
                "id_categoria INTEGER NOT NULL, " +
                "id_estado INTEGER NOT NULL, " +
                "FOREIGN KEY (id_persona) REFERENCES Persona(idPersona), " +
                "FOREIGN KEY (id_categoria) REFERENCES Categoria(idCategoria), " +
                "FOREIGN KEY (id_estado) REFERENCES Estado(idEstado))");

        // Tabla Estado
        db.execSQL("CREATE TABLE Estado (" +
                "idEstado INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL )");

        // Tabla Categoria
        db.execSQL("CREATE TABLE Categoria (" +
                "idCategoria INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL)");

        ContentValues RegCat = new ContentValues();
        RegCat.put("nombre", "Accidente");
        db.insert("Categoria", null, RegCat);

        RegCat.put("nombre", "Pelea");
        db.insert("Categoria", null, RegCat);

        RegCat.put("nombre", "Otro");
        db.insert("Categoria", null, RegCat);

        ContentValues RegEst = new ContentValues();
        RegEst.put("nombre", "Enviado");
        db.insert("Estado", null, RegEst);

        RegEst.put("nombre", "En revisión");
        db.insert("Estado", null, RegEst);

        RegEst.put("nombre", "Realizado");
        db.insert("Estado", null, RegEst);

    }
    // Método para insertar una nueva categoría
    public boolean insertarCategoria(String nombre, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre", nombre);
        contentValues.put("descripcion", descripcion);
        long result = db.insert("Categoria", null, contentValues);
        db.close();
        return result != -1;
    }
    // Método para verificar la inserción
    public void verificarInsercion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categoria", null);
        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                System.out.println("Categoria: " + nombre + ", Descripcion: " + descripcion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Denuncia");
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS Persona");
        db.execSQL("DROP TABLE IF EXISTS Estado");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        onCreate(db);
    }
}
