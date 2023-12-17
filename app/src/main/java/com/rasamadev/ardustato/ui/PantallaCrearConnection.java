package com.rasamadev.ardustato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.Connection;
import com.rasamadev.ardustato.models.User;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

public class PantallaCrearConnection extends AppCompatActivity {
    private EditText etNombreConnection_PantallaCrearConnection, etIpConnection_PantallaCrearConnection;

//    private String idUserSesionIniciada;
    private OperacionesBaseDatos datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearconnection);

        // RECUPERAMOS EL ID DEL USUARIO QUE HA INICIADO SESION PARA QUE, AL CREAR UNA CONNECTION,
        // SE ASOCIE A DICHO USUARIO
//        Bundle bundle = getIntent().getExtras();
//        idUserSesionIniciada = bundle.getString("id");

        etNombreConnection_PantallaCrearConnection = findViewById(R.id.etNombreConnection_PantallaCrearConnection);
        etIpConnection_PantallaCrearConnection = findViewById(R.id.etIpConnection_PantallaCrearConnection);

        // INSTANCIA A LA BBDD
        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void probarConnection(View view) {
        String ip = etIpConnection_PantallaCrearConnection.getText().toString();

        // SI NO HEMOS ESCRITO NADA EN EL CAMPO DE LA IP
        if(ip.equals("")){
            AlertDialogsUtil.mostrarError(this,"Por favor, introduce una direccion IP.");
        }
        else{
            // PRUEBA DE CONEXION A LA IP ESCRITA
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + ip + "/checkconnection";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
//                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                        AlertDialogsUtil.mostrarError(this,response);
                    },
                    error -> {
//                    Toast.makeText(this, "No se ha podido conectar a la IP: ." + url + ". CAUSA: " + error, Toast.LENGTH_SHORT).show();
                        AlertDialogsUtil.mostrarError(this,"No se ha podido conectar a la IP: " + ip + ".\nCAUSA: " + error);
                    });

            queue.add(stringRequest);
        }
    }

    public void guardarConnection(View view) {
        String nombreconnection = etNombreConnection_PantallaCrearConnection.getText().toString();
        String ipconnection = etIpConnection_PantallaCrearConnection.getText().toString();

        if(nombreconnection.equals("") || ipconnection.equals("")){
            AlertDialogsUtil.mostrarError(this,"Por favor, introduce todos los datos.");
        }
        else{
            // COMPROBAR EN LA TABLA connections SI EXISTE UNA CONEXION CON MISMO NOMBRE E IP
            if(comprobarConnectionExistente(nombreconnection,ipconnection)){
                AlertDialogsUtil.mostrarError(this,"¡YA EXISTE UNA CONEXION CON EL MISMO NOMBRE E IP!");
            }
            else{
                // INSERTAMOS LA CONEXION
                datos.insertarConnection(nombreconnection,ipconnection,PantallaIniciarSesion.ID_USER);
                Toast.makeText(this, "Conexion creada con exito.", Toast.LENGTH_LONG).show();

                // VOLVEMOS ATRAS CARGANDO DE NUEVO LAS CONEXIONES
//                getOnBackPressedDispatcher().onBackPressed();
                Intent i = new Intent(this, PantallaConnections.class);
                i.putExtra("id",PantallaIniciarSesion.ID_USER);
                startActivity(i);
            }
        }
    }

    private boolean comprobarConnectionExistente(String nombreconnection, String ipconnection){
        boolean existe = false;

        for(Connection c: datos.selectConnectionsByUser(PantallaIniciarSesion.ID_USER)){
            if(c.getConnectionname().equals(nombreconnection) && c.getIp().equals(ipconnection)){
                existe = true;
                break;
            }
        }

        return existe;
    }
}
