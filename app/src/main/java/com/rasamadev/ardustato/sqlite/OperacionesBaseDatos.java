package com.rasamadev.ardustato.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rasamadev.ardustato.models.User;

import java.util.ArrayList;
import java.util.List;

public class OperacionesBaseDatos {
    private static BaseDatosArdustato baseDatos;
    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();

    private OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if(baseDatos == null){
            baseDatos = new BaseDatosArdustato(contexto);
        }

        return instancia;
    }

    public List<User> selectUsers(){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {"id","fullname","mail","pass"};

        Cursor cursor = db.query("users",null,null,null,null,null,null);
        List<User> users = new ArrayList<>();
        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            String mail = cursor.getString(cursor.getColumnIndexOrThrow("mail"));
            String pass = cursor.getString(cursor.getColumnIndexOrThrow("pass"));

            User u = new User(id,fullname,mail,pass);
            users.add(u);
        }
        cursor.close();

        return users;
    }

    public void insertarUser(String fullname, String mail, String pass){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("mail", mail);
        values.put("pass", pass);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("users", null, values);
        Log.d("CREADO USUARIO: ",Long.toString(newRowId));
    }
}
