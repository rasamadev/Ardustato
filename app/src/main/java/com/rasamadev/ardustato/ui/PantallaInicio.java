package com.rasamadev.ardustato.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}