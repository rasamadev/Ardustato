package com.rasamadev.ardustato.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.Connection;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

public class PantallaCrearConnection extends AppCompatActivity {

    // ELEMENTOS PANTALLA
    private EditText etNombreConnection_PantallaCrearConnection, etIpConnection_PantallaCrearConnection;

    // INSTANCIA BASE DATOS
    private OperacionesBaseDatos datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearconnection);

        etNombreConnection_PantallaCrearConnection = findViewById(R.id.etNombreConnection_PantallaCrearConnection);
        etIpConnection_PantallaCrearConnection = findViewById(R.id.etIpConnection_PantallaCrearConnection);

        // INSTANCIA A LA BBDD
        datos = OperacionesBaseDatos.obtenerInstancia(this);
    }

    public void probarConnection(View view) {
        String ip = etIpConnection_PantallaCrearConnection.getText().toString();

        // SI NO HEMOS ESCRITO NADA EN EL CAMPO DE LA IP
        if(ip.equals("")){
            AlertDialogsUtil.mostrarMensaje(this,"ERROR","Por favor, introduce una direccion IP.");
        }
        else{
            // PRUEBA DE CONEXION A LA IP ESCRITA
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://" + ip + "/checkconnection";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    AlertDialogsUtil.mostrarMensaje(this,"EXITO",response);
                },
                error -> {
                    AlertDialogsUtil.mostrarMensaje(this,"ERROR","No se ha podido conectar a la IP: " + ip + ".\nCAUSA: " + error);
                }
            );

            queue.add(stringRequest);
        }
    }

    public void guardarConnection(View view) {
        String nombreconnection = etNombreConnection_PantallaCrearConnection.getText().toString();
        String ipconnection = etIpConnection_PantallaCrearConnection.getText().toString();

        if(nombreconnection.equals("") || ipconnection.equals("")){
            AlertDialogsUtil.mostrarMensaje(this,"ERROR","Por favor, introduce todos los datos.");
        }
        else{
            // COMPROBAR EN LA TABLA connections SI EXISTE UNA CONEXION CON MISMO NOMBRE E IP
            if(comprobarConnectionExistente(nombreconnection,ipconnection)){
                AlertDialogsUtil.mostrarMensaje(this,"ERROR","¡YA EXISTE UNA CONEXION CON EL MISMO NOMBRE E IP!");
            }
            else{
                // INSERTAMOS LA CONEXION
                datos.insertarConnection(nombreconnection,ipconnection,PantallaIniciarSesion.ID_USER);
                Toast.makeText(this, "Conexion creada con exito.", Toast.LENGTH_LONG).show();

                // VOLVEMOS ATRAS CARGANDO DE NUEVO LAS CONEXIONES
                Intent i = new Intent(this, PantallaConnections.class);
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
