package com.rasamadev.ardustato.ui;

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
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

public class PantallaConnection extends AppCompatActivity {

    // ELEMENTOS PANTALLA
    private EditText etNumero_PantallaConnection;
    private TextView tvTempActual_PantallaConnection;
    private TextView tvTempDeseada_PantallaConnection;

    // ATRIBUTOS BUNDLE
    private String ip;
    private String tempActual;
    private String tempDeseada;

    // OTROS ATRIBUTOS
    private float tempDeseadaFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);

        Bundle bundle = getIntent().getExtras();
        ip = bundle.getString("ip");
        tempActual = bundle.getString("tempActual");
        tempDeseada = bundle.getString("tempDeseada");

        etNumero_PantallaConnection = findViewById(R.id.etNumero_PantallaConnection);
        etNumero_PantallaConnection.setKeyListener(null); // PARA QUE NO SE PUEDA INSERTAR UN NUMERO
        tvTempActual_PantallaConnection = findViewById(R.id.tvTempActual_PantallaConnection);
        tvTempDeseada_PantallaConnection = findViewById(R.id.tvTempDeseada_PantallaConnection);

        tempDeseadaFloat = Float.parseFloat(tempDeseada);

        etNumero_PantallaConnection.setText(tempDeseada);
        tvTempActual_PantallaConnection.setText(tempActual);
        tvTempDeseada_PantallaConnection.setText(tempDeseada);
    }


    public void EnviarNumero(View view) {
//        Log.d(TAG,"NUMERO ENVIADO");

        // PONEMOS EN EL CAMPO DE TEMPERATURA DESEADA LA TEMPERATURA QUE ACABAMOS DE PROGRAMAR
        tvTempDeseada_PantallaConnection.setText(etNumero_PantallaConnection.getText().toString());

        // CONVERTIMOS A FLOAT LA TEMPERATURA QUE ACABAMOS DE PROGRAMAR
        Float temperaturaprogramada = Float.parseFloat(etNumero_PantallaConnection.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/settemperatura?temperatura=" + temperaturaprogramada;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            response -> {
                AlertDialogsUtil.mostrarMensaje(this,"ENVIADO","Respuesta del servidor:\n" + response);
            },
            error -> {
                AlertDialogsUtil.mostrarMensaje(this,"ERROR","No se ha podido enviar la temperatura.\nCausa del error: " + error);
            }
        );

        queue.add(stringRequest);
    }

    public void bajarTemperatura(View view) {
        tempDeseadaFloat-=0.50;
        tempDeseada = Float.toString(tempDeseadaFloat);
        etNumero_PantallaConnection.setText(tempDeseada);
    }

    public void subirTemperatura(View view) {
        tempDeseadaFloat+=0.50;
        tempDeseada = Float.toString(tempDeseadaFloat);
        etNumero_PantallaConnection.setText(tempDeseada);
    }

    public void ActualizarTemp(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/gettemperatura";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            response -> {
               if(response.equals("nan")){
                   tvTempActual_PantallaConnection.setText("ERROR");
               }
               else{
                   tvTempActual_PantallaConnection.setText(response);
               }
            },
            error -> {
                Toast.makeText(this, "Error al actualizar la temperatura! " + error, Toast.LENGTH_SHORT).show();
            }
        );

        queue.add(stringRequest);
    }
}
