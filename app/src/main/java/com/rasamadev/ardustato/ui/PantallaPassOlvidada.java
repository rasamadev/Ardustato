package com.rasamadev.ardustato.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

public class PantallaPassOlvidada extends AppCompatActivity {

    private EditText etMail_PassOlvidada;

    private OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passolvidada);

        etMail_PassOlvidada = findViewById(R.id.etMail_PassOlvidada);

        // OBTENER INSTANCIA BBDD
        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void EnviarPass(View view) {
        String mail = etMail_PassOlvidada.getText().toString();
        String pass;

        // SI NO HA ESCRITO NADA
        if(mail.equals("")){
            AlertDialogsUtil.mostrarMensaje(this,"ERROR","Por favor, introduce tu correo electronico.");
        }
        else{
            pass = datos.selectPassByMail(mail);
            if(pass.equals("")){
                AlertDialogsUtil.mostrarMensaje(this,"ERROR","No existe ningun usuario con ese correo.");
            }
            else{
//                AlertDialogsUtil.mostrarError(this,"CONTRASEÑA: " + pass);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("CONTRASEÑA ENVIADA");
                builder.setMessage("Se ha enviado la contraseña a su cuenta de correo. Por motivos de seguridad, asegurese de cambiarla nada mas iniciar sesion en la aplicacion.");
                builder.setCancelable(false);
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // VOLVEMOS ATRAS;
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                });
                builder.create();
                builder.show();
            }
        }
    }
}
