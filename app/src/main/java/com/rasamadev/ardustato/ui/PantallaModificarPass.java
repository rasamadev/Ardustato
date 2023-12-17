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

public class PantallaModificarPass extends AppCompatActivity {

    private EditText etPass_ModificarPass,etNuevaPass_ModificarPass,etConfirmarPass_ModificarPass;

    private OperacionesBaseDatos datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificarpass);

        etPass_ModificarPass = findViewById(R.id.etPass_ModificarPass);
        etNuevaPass_ModificarPass = findViewById(R.id.etNuevaPass_ModificarPass);
        etConfirmarPass_ModificarPass = findViewById(R.id.etConfirmarPass_ModificarPass);

        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void ModificarPass(View view) {
        String pass = etPass_ModificarPass.getText().toString();
        String nuevapass = etNuevaPass_ModificarPass.getText().toString();
        String confirmarpass = etConfirmarPass_ModificarPass.getText().toString();

        // SI LA NUEVA CONTRASEÑA Y LA "CONFIRMAR CONTRASEÑA" NO COINCIDEN
        if(!nuevapass.equals(confirmarpass)){
            AlertDialogsUtil.mostrarError(this,"¡Las contraseñas nuevas no coinciden!");
        }
        // SI LA CONTRASEÑA VIEJA INTRODUCIDA NO ES IGUAL QUE LA CONTRASEÑA VIEJA ESTABLECIDA
        else if(!pass.equals(PantallaIniciarSesion.PASS_USER)){
            AlertDialogsUtil.mostrarError(this,"La contraseña actual que has introducido es incorrecta.");
        }
        else{
            // MODIFICAR PASS
            datos.updatePassById(PantallaIniciarSesion.ID_USER,confirmarpass);

            // MODIFICAR VARIABLES GLOBALES
            PantallaIniciarSesion.PASS_USER = confirmarpass;

            // TOAST
            Toast.makeText(this, "Contraseña modificada con exito.", Toast.LENGTH_LONG).show();

            // VOLVER A LA PANTALLA DE CONEXIONES
            Intent i = new Intent(this, PantallaConnections.class);
            startActivity(i);
        }
    }
}
