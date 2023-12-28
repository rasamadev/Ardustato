package com.rasamadev.ardustato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.User;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;

public class PantallaIniciarSesion extends AppCompatActivity {

    // ELEMENTOS PANTALLA
    private EditText etCorreo_IniciarSesion, etPass_IniciarSesion;
    private TextView tvOlvidePass_IniciarSesion;

    // INSTANCIA BASE DATOS
    private OperacionesBaseDatos datos;

    // VARIABLES GLOBALES DATOS USUARIO QUE USAREMOS DURANTE LA SESION DEL USUARIO
    public static String ID_USER;
    public static String FULLNAME_USER;
    public static String MAIL_USER;
    public static String PASS_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciarsesion);

        etCorreo_IniciarSesion = findViewById(R.id.etCorreo_IniciarSesion);
        etPass_IniciarSesion = findViewById(R.id.etPass_IniciarSesion);
        tvOlvidePass_IniciarSesion = findViewById(R.id.tvOlvidePass_IniciarSesion);

        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void IniciarSesion(View view) {
        String mail = etCorreo_IniciarSesion.getText().toString();
        String pass = etPass_IniciarSesion.getText().toString();

        // COMPROBAMOS USER Y PASSWORD EN LA BBDD.
        // SI LOS DATOS SON CORRECTOS
        if(datos.comprobacionUser(mail,pass)){

            // RECOGEMOS TODOS LOS DATOS DEL USUARIO INICIADO EN LAS VARIABLES GLOBALES
            // Y CARGAMOS LA PANTALLA DE CONEXIONES
            ID_USER = datos.selectIdUserByMailAndPass(mail,pass);
            FULLNAME_USER = datos.selectFullnameByMailAndPass(mail,pass);
            MAIL_USER = mail;
            PASS_USER = pass;

            Intent i = new Intent(this,PantallaConnections.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Usuario y/o contraseña incorrect@(s).", Toast.LENGTH_SHORT).show();
        }
    }

    public void OlvidePass(View view) {
        Intent i = new Intent(this,PantallaPassOlvidada.class);
        startActivity(i);
    }
}
