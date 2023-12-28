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

    // ELEMENTOS PANTALLA
    private EditText etFullname_ModificarPerfil, etMail_ModificarPerfil;

    // INSTANCIA BASE DATOS
    private OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificarperfil);

        etFullname_ModificarPerfil = findViewById(R.id.etFullname_ModificarPerfil);
        etMail_ModificarPerfil = findViewById(R.id.etMail_ModificarPerfil);

        // ESTABLECEMOS EN LOS EditText DE LA PANTALLA EL NOMBRE Y CORREO DEL USUARIO
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
            AlertDialogsUtil.mostrarMensaje(this,"ERROR","¡NO HAS MODIFICADO NADA!");
        }
        // SI ESCRIBIMOS OTRO CORREO DIFERENTE AL NUESTRO Y YA EXISTE EN LA BBDD.
        // ESTO SE HACE ASI PARA QUE, SI NO MODIFICAMOS EL CORREO, NOS DEJE CONTINUAR
        // Y NO VEA QUE, EVIDENTEMENTE, YA EXISTE EN LA BBDD
        else if(mailexistente.equals(mail) && !mail.equals(PantallaIniciarSesion.MAIL_USER)){
            AlertDialogsUtil.mostrarMensaje(this,"ERROR","Ya existe una cuenta con la direccion de correo proporcionada. Por favor, indique otra cuenta diferente.");
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
