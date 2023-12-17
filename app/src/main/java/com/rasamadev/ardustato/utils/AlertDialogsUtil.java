package com.rasamadev.ardustato.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogsUtil {
    public static void mostrarError(Context ctx, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("ERROR");
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("ACEPTAR",null);
        builder.create();
        builder.show();
    }

    public static void enviarCorreo(Context ctx){

    }
}
