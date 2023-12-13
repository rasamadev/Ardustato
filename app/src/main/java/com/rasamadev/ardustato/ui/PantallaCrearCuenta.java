package com.rasamadev.ardustato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.User;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;

public class PantallaCrearCuenta extends AppCompatActivity {
    private EditText etNombreCompleto_CrearCuenta, etCorreo_CrearCuenta, etPass_CrearCuenta;
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

        if(fullname.equals("") || mail.equals("") || pass.equals("")){
            Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show();
        }
        else{
            // COMPROBAR SI YA EXISTE UNA CUENTA CON EL CORREO ELECTRONICO INTRODUCIDO
            if(comprobacionMail(mail)){
                Toast.makeText(this, "Ya existe una cuenta con la direccion de correo proporcionada.", Toast.LENGTH_LONG).show();
            }
            else{
                datos.insertarUser(fullname,mail,pass);
                Toast.makeText(this, "Cuenta creada con exito.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, PantallaInicio.class);
                startActivity(i);
            }
        }
    }

    private boolean comprobacionMail(String mail){
        boolean existe = false;

        for(User u: datos.selectUsers()){
            if(u.getMail().equals(mail)){
                existe = true;
                break;
            }
        }

        return existe;
    }
}
