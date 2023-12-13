package com.rasamadev.ardustato.ui;

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
    private EditText etCorreo_IniciarSesion, etPass_IniciarSesion;
    private TextView tvOlvidePass_IniciarSesion;
    private OperacionesBaseDatos datos;

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

        // FUNCION ESTATICA QUE COMPRUEBE USER Y PASSWORD EN LA BBDD
        if(comprobarUser(mail,pass)){
            Toast.makeText(this, "Iniciado sesion correctamente.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Usuario y/o contraseña incorrecto(s).", Toast.LENGTH_SHORT).show();
        }

    }

    public void OlvidePass(View view) {
        Toast.makeText(this, "Ir a pantalla Olvide la pass", Toast.LENGTH_SHORT).show();
    }

    private boolean comprobarUser(String mail, String pass){
        boolean existe = false;

        for(User u: datos.selectUsers()){
            if(u.getMail().equals(mail) && u.getPass().equals(pass)){
                existe = true;
                break;
            }
        }

        return existe;
    }
}
