package com.rasamadev.ardustato.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class BaseDatosArdustato extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "ardustato.db";
    private static final int VERSION_ACTUAL = 1;

    public BaseDatosArdustato(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            }
            else{
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, fullname VARCHAR(30), mail VARCHAR(30), pass VARCHAR(30))");
        db.execSQL("CREATE TABLE connections(id INTEGER PRIMARY KEY AUTOINCREMENT, connectionname VARCHAR(20), ip VARCHAR(20), userid INT, FOREIGN KEY (userid) REFERENCES users(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS connections");

        onCreate(db);
    }
}
