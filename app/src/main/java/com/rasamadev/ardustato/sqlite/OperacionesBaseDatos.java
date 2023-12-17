package com.rasamadev.ardustato.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.rasamadev.ardustato.models.Connection;
import com.rasamadev.ardustato.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Usa patron Singleton.
 */
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
        // you will actually use after this query. (NO SE USA, SE PONE null)
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

//    public User selectUserById(String idUser){
//        SQLiteDatabase db = baseDatos.getReadableDatabase();
//
//        User u = new User("","","","");
//
//        // Filter results WHERE "id" = 'idUser'
//        String selection = "id = ?";
//        String[] selectionArgs = {idUser};
//
//        Cursor cursor = db.query("users",null,selection,selectionArgs,null,null,null);
//        while(cursor.moveToNext()) {
//            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
//            String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
//            String mail = cursor.getString(cursor.getColumnIndexOrThrow("mail"));
//            String pass = cursor.getString(cursor.getColumnIndexOrThrow("pass"));
//
//            u.setId(id);
//            u.setFullname(fullname);
//            u.setMail(mail);
//            u.setPass(pass);
//        }
//        cursor.close();
//
//        Log.d("METODO DEVOLVER USER, DEVUELVE",u.toString());
//        return u;
//    }

    public List<Connection> selectConnectionsByUser(String idUser){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] projection = {"id","connectionname","ip","userid"};

        // Filter results WHERE "userid" = 'idUser'
        String selection = "userid = ?";
        String[] selectionArgs = {idUser};

        Cursor cursor = db.query("connections",null,selection,selectionArgs,null,null,null);
        List<Connection> connections = new ArrayList<>();
        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String connectionname = cursor.getString(cursor.getColumnIndexOrThrow("connectionname"));
            String ip = cursor.getString(cursor.getColumnIndexOrThrow("ip"));
            String userid = cursor.getString(cursor.getColumnIndexOrThrow("userid"));

            Connection c = new Connection(id,connectionname,ip,userid);
            connections.add(c);
        }
        cursor.close();

        return connections;
    }

    public String selectPassByMail(String mail){
        String pass = "";

        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] projection = {"pass"};

        // Filter results WHERE "userid" = 'idUser'
        String selection = "mail = ?";
        String[] selectionArgs = {mail};

        Cursor cursor = db.query("users",projection,selection,selectionArgs,null,null,null);
//        List<Connection> connections = new ArrayList<>();
        while(cursor.moveToNext()) {
            pass = cursor.getString(cursor.getColumnIndexOrThrow("pass"));
        }
        cursor.close();

        return pass;
    }

    public String selectIdUserByMailAndPass(String mail, String pass){
        String id = "";

        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] projection = {"id"};

        // Filter results WHERE "userid" = 'idUser'
        String selection = "mail = ? AND pass = ?";
        String[] selectionArgs = {mail,pass};

        Cursor cursor = db.query("users",projection,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();

        return id;
    }

    public String selectFullnameByMailAndPass(String mail, String pass){
        String fullname = "";

        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] projection = {"fullname"};

        // Filter results WHERE "userid" = 'idUser'
        String selection = "mail = ? AND pass = ?";
        String[] selectionArgs = {mail,pass};

        Cursor cursor = db.query("users",projection,selection,selectionArgs,null,null,null);
        while(cursor.moveToNext()) {
            fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
        }
        cursor.close();

        return fullname;
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

    public void insertarConnection(String connectionname, String ip, String userid){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("connectionname", connectionname);
        values.put("ip", ip);
        values.put("userid", userid);

        Log.d("VALUES: ",values.toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert("connections", null, values);

        Log.d("CREADA CONEXION: ",Long.toString(newRowId));
    }

    public void updateUserById(String id, String fullname, String mail){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("mail", mail);

        // Which row to update, based on the title
        String selection = "id = ?";
        String[] selectionArgs = {id};

        int count = db.update("users", values, selection, selectionArgs);
    }

    public void updatePassById(String id, String pass){
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put("pass", pass);

        // Which row to update, based on the title
        String selection = "id = ?";
        String[] selectionArgs = {id};

        int count = db.update("users", values, selection, selectionArgs);
    }

    public void deleteUserById(String id){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        // Define 'where' part of query.
        String selection = "id = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int deletedRows = db.delete("users", selection, selectionArgs);
    }

    public int deleteConnectionById(String id){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        // Define 'where' part of query.
        String selection = "id = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {id};
        // Issue SQL statement.
        int deletedRows = db.delete("connections", selection, selectionArgs);
        return deletedRows;
    }

    public boolean comprobacionUser(String mail, String pass){
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Filter results WHERE "mail" = 'mail' AND "pass" = "pass"
        String selection = "mail = ? AND pass = ?";
        String[] selectionArgs = {mail,pass};

        Cursor cursor = db.query("users",null,selection,selectionArgs,null,null,null);
        if(cursor.getCount() == 1){
            cursor.close();
            return true;
        }
        else if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        else{
            // HA ENCONTRADO MAS DE UN USUARIO CON MISMO NOMBRE Y PASS???
            // DEVOLVIENDO TRUE PROBABLEMENTE LA APLICACION DARA ERROR
            cursor.close();
            return true;
        }
    }

    public String comprobacionMail(String mail){
        String mailexistente = "";

        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String[] projection = {"mail"};

        // Filter results WHERE "mail" = 'mail'
        String selection = "mail = ?";
        String[] selectionArgs = {mail};

        Cursor cursor = db.query("users",projection,selection,selectionArgs,null,null,null);
        // SI NO ENCUENTRA NINGUN RESULTADO, NO ENTRARA EN EL WHILE
        while(cursor.moveToNext()) {
            mailexistente = cursor.getString(cursor.getColumnIndexOrThrow("mail"));
        }
        cursor.close();

        return mailexistente;
    }
}
