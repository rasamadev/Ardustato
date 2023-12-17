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
    private EditText etCorreo_IniciarSesion, etPass_IniciarSesion;
    private TextView tvOlvidePass_IniciarSesion;
    private OperacionesBaseDatos datos;

    // VARIABLES GLOBALES DATOS USUARIO
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

        // COMPROBAMOS USER Y PASSWORD EN LA BBDD
        if(datos.comprobacionUser(mail,pass)){
//            i.putExtra("id",recogerIdUser(mail,pass));
//            i.putExtra("fullname",recogerFullnameUser(mail,pass));
//            i.putExtra("mail",mail);
//            i.putExtra("pass",pass);

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
//        Toast.makeText(this, "Ir a pantalla Olvide la pass", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,PantallaPassOlvidada.class);
        startActivity(i);
    }

//    private boolean comprobarUser(String mail, String pass){
//        boolean existe = false;
//
//        for(User u: datos.selectUsers()){
//            if(u.getMail().equals(mail) && u.getPass().equals(pass)){
//                existe = true;
//                break;
//            }
//        }
//
//        return existe;
//    }
//
//    private String recogerIdUser(String mail, String pass){
//        String idUsuarioSesionIniciada = "";
//
//        for(User u: datos.selectUsers()){
//            if(u.getMail().equals(mail) && u.getPass().equals(pass)){
//                idUsuarioSesionIniciada = u.getId();
//                break;
//            }
//        }
//
//        return idUsuarioSesionIniciada;
//    }
//
//    private String recogerFullnameUser(String mail, String pass){
//        String fullnameUsuarioSesionIniciada = "";
//
//        for(User u: datos.selectUsers()){
//            if(u.getMail().equals(mail) && u.getPass().equals(pass)){
//                fullnameUsuarioSesionIniciada = u.getFullname();
//                break;
//            }
//        }
//
//        return fullnameUsuarioSesionIniciada;
//    }
}
