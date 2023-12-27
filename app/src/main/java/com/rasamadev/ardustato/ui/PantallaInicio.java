package com.rasamadev.ardustato.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.rasamadev.ardustato.R;

public class PantallaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallainicio);
    }

    public void PantallaIniciarSesion(View view) {
        Intent i = new Intent(this, PantallaIniciarSesion.class);
        startActivity(i);
    }

    public void PantallaCrearCuenta(View view) {
        Intent i = new Intent(this, PantallaCrearCuenta.class);
        startActivity(i);
    }

    // CONTROL DE ACCIONES AL PULSAR EL BOTON "ATRAS"
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        return super.onKeyDown(keyCode, event);
    }
}