package com.rasamadev.ardustato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.User;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;

public class PantallaCrearCuenta extends AppCompatActivity {

    // ELEMENTOS PANTALLA
    private EditText etNombreCompleto_CrearCuenta, etCorreo_CrearCuenta, etPass_CrearCuenta;

    // INSTANCIA BASE DATOS
    private OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearcuenta);

        etNombreCompleto_CrearCuenta = findViewById(R.id.etNombreCompleto_CrearCuenta);
        etCorreo_CrearCuenta = findViewById(R.id.etCorreo_CrearCuenta);
        etPass_CrearCuenta = findViewById(R.id.etPass_CrearCuenta);

        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void CrearCuenta(View view) {
        String fullname = etNombreCompleto_CrearCuenta.getText().toString();
        String mail = etCorreo_CrearCuenta.getText().toString();
        String pass = etPass_CrearCuenta.getText().toString();

        // SI NO SE HAN RELLENADO TODOS LOS CAMPOS
        if(fullname.equals("") || mail.equals("") || pass.equals("")){
            Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show();
        }
        else{
            // COMPROBAMOS EN LA BBDD SI YA EXISTE UNA CUENTA CON EL CORREO ELECTRONICO INTRODUCIDO.
            String mailexistente = datos.comprobacionMail(mail);

            if(mailexistente.equals(mail)){
                Toast.makeText(this, "Ya existe una cuenta con la direccion de correo proporcionada.", Toast.LENGTH_LONG).show();
            }
            else{
                // INSERTAMOS EL USUARIO
                datos.insertarUser(fullname,mail,pass);
                Toast.makeText(this, "Cuenta creada con exito.", Toast.LENGTH_LONG).show();

                // VOLVEMOS ATRAS;
                getOnBackPressedDispatcher().onBackPressed();
            }
        }
    }
}
