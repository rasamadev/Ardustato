package com.rasamadev.ardustato.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogsUtil {
    public static void mostrarMensaje(Context ctx, String title, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("ACEPTAR",null);
        builder.create();
        builder.show();
    }
}
