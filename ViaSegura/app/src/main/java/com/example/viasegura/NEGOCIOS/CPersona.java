package com.example.viasegura.NEGOCIOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.viasegura.DATOS.CBaseDatos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CPersona {
    public void GuardarPer(Context Contexto, String Nom, String Ape, String Ced,int Tel)
    {
        // Obtener la fecha actual
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String fechaHoy = formatter.format(new Date());

        CBaseDatos Obj1 = new CBaseDatos(Contexto);
        SQLiteDatabase Obj2 = Obj1.getWritableDatabase();
        ContentValues Reg = new ContentValues();
        Reg.put("nombre",Nom);
        Reg.put("apellido",Ape);
        Reg.put("cedula",Ced);
        Reg.put("telefono",Tel);
        Reg.put("fecha", fechaHoy);
        Obj2.insert("Persona",null,Reg);
        Obj1.close();
    }
}

