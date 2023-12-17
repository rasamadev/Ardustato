package com.rasamadev.ardustato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

public class PantallaModificarPerfil extends AppCompatActivity {

    private EditText etFullname_ModificarPerfil, etMail_ModificarPerfil;

    private OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificarperfil);

        etFullname_ModificarPerfil = findViewById(R.id.etFullname_ModificarPerfil);
        etMail_ModificarPerfil = findViewById(R.id.etMail_ModificarPerfil);

        etFullname_ModificarPerfil.setText(PantallaIniciarSesion.FULLNAME_USER);
        etMail_ModificarPerfil.setText(PantallaIniciarSesion.MAIL_USER);

        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void AplicarCambios(View view) {
        String fullname = etFullname_ModificarPerfil.getText().toString();
        String mail = etMail_ModificarPerfil.getText().toString();

        String mailexistente = datos.comprobacionMail(mail);

        // SI EL USUARIO NO HA MODIFICADO NADA
        if(fullname.equals(PantallaIniciarSesion.FULLNAME_USER) && mail.equals(PantallaIniciarSesion.MAIL_USER)){
            AlertDialogsUtil.mostrarError(this,"¡NO HAS MODIFICADO NADA!");
        }
        // SI SE INTENTA CAMBIAR EL CORREO A UNO QUE YA EXISTE EN LA BBDD
        else if(mailexistente.equals(mail)){
            Toast.makeText(this, "Ya existe una cuenta con la direccion de correo proporcionada.", Toast.LENGTH_LONG).show();
        }
        else{
            // MODIFICAR PERFIL
            datos.updateUserById(PantallaIniciarSesion.ID_USER,fullname,mail);

            // MODIFICAR VARIABLES GLOBALES
            PantallaIniciarSesion.FULLNAME_USER = fullname;
            PantallaIniciarSesion.MAIL_USER = mail;

            // TOAST
            Toast.makeText(this, "Usuario modificado con exito.", Toast.LENGTH_LONG).show();

            // VOLVER A LA PANTALLA DE CONEXIONES
            Intent i = new Intent(this, PantallaConnections.class);
            startActivity(i);
        }
    }
}
