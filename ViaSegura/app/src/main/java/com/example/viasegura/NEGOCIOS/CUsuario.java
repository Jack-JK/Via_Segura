package com.example.viasegura.NEGOCIOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.viasegura.DATOS.CBaseDatos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CUsuario {
    public int traerIdPersona(Context Contexto) {
        CBaseDatos Obj1 = new CBaseDatos(Contexto);
        SQLiteDatabase Obj2 = Obj1.getReadableDatabase();
        int IdPer = -1;
        Cursor Filas = Obj2.rawQuery("Select Max(idPersona) From Persona", null);
        if (Filas.moveToFirst())
            IdPer = Filas.getInt(0);
        Obj1.close();
        return IdPer;
    }

    public void GuardarUsu(Context Contexto, String Cor, String Cont) {
        CBaseDatos Obj1 = new CBaseDatos(Contexto);
        SQLiteDatabase Obj2 = Obj1.getWritableDatabase();
        ContentValues Reg = new ContentValues();
        Reg.put("correo", Cor);
        Reg.put("contrasena", Cont);
        Reg.put("id_persona", traerIdPersona(Contexto));
        Obj2.insert("Usuario", null, Reg);  // Asegúrate de que la tabla sea Usuario, no Persona
        Obj1.close();
    }
}
