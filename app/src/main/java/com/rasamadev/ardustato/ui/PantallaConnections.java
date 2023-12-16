package com.rasamadev.ardustato.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.Connection;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AdapterConnections;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

import java.util.ArrayList;
import java.util.List;

public class PantallaConnections extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView lista;
    private List<Connection> connectionsList;
    private OperacionesBaseDatos datos;
    private String idUserSesionIniciada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connections);

        lista = findViewById(R.id.listViewConnections);

        // RECUPERAMOS EL ID DEL USUARIO QUE HA INICIADO SESION
        Bundle bundle = getIntent().getExtras();
        idUserSesionIniciada = bundle.getString("id");

        datos = OperacionesBaseDatos.obtenerInstancia(this);

        // HACEMOS UNA CONSULTA DE LAS CONEXIONES ASIGNADAS AL USUARIO INICIADO
        // Y LAS CARGAMOS EN LA LISTVIEW
        connectionsList = datos.selectConnectionsByUser(idUserSesionIniciada);
        AdapterConnections adapterconnections = new AdapterConnections(this, R.layout.connection_item, connectionsList);
        lista.setAdapter(adapterconnections);

        lista.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ip = connectionsList.get(position).getIp();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/checkconnection";
//        Log.d("IP A LA QUE ME INTENTO CONECTAR: ",url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
//                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, PantallaConnection.class);
                    i.putExtra("ip",ip);
                    startActivity(i);
                },
                error -> {
//                    Toast.makeText(this, "No se ha podido conectar a la IP: ." + url + ". CAUSA: " + error, Toast.LENGTH_SHORT).show();
                    AlertDialogsUtil.mostrarError(this,"No se ha podido conectar a la IP: " + ip + ".\nCAUSA: " + error);
                });

        queue.add(stringRequest);
    }

    // BOTON FLOTANTE, LLEVA A LA PANTALLA PARA CREAR UNA NUEVA CONEXION
    public void PantallaCrearConnection(View view) {
        Intent i = new Intent(this,PantallaCrearConnection.class);
        i.putExtra("id",idUserSesionIniciada);
        startActivity(i);
    }
}
