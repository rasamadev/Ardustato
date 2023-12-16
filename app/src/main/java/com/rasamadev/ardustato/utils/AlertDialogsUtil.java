package com.rasamadev.ardustato.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogsUtil {
    public static void mostrarError(Context ctx, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
//        builder.setTitle("ERROR");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }
}
