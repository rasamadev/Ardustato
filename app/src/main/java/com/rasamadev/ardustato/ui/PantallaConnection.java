package com.rasamadev.ardustato.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rasamadev.ardustato.R;

public class PantallaConnection extends AppCompatActivity {
    private EditText etNumero_PantallaConnection;
    private TextView tvRespuesta_PantallaConnection;
    private TextView tvTempActual_PantallaConnection;
    private TextView tvTempDeseada_PantallaConnection;
    private final String TAG = ":::TAG: ";

    private String ip;
    private String tempActual;
    private float tempDeseada = 20.0F;
    private String tempDeseadaString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);

        Bundle bundle = getIntent().getExtras();
        ip = bundle.getString("ip");
        tempActual = bundle.getString("tempActual");

        etNumero_PantallaConnection = findViewById(R.id.etNumero_PantallaConnection);
        etNumero_PantallaConnection.setKeyListener(null); // PARA QUE NO SE PUEDA INSERTAR UN NUMERO
        tvRespuesta_PantallaConnection = findViewById(R.id.tvRespuesta_PantallaConnection);
        tvTempActual_PantallaConnection = findViewById(R.id.tvTempActual_PantallaConnection);
        tvTempDeseada_PantallaConnection = findViewById(R.id.tvTempDeseada_PantallaConnection);

        tempDeseadaString = Float.toString(tempDeseada);
        etNumero_PantallaConnection.setText(tempDeseadaString);

        tvTempActual_PantallaConnection.setText("Temperatura actual: " + tempActual);
    }


    public void EnviarNumero(View view) {
        Log.d(TAG,"NUMERO ENVIADO");
        tvTempDeseada_PantallaConnection.setText("Temperatura deseada: " + etNumero_PantallaConnection.getText().toString());

        Float numero = Float.parseFloat(etNumero_PantallaConnection.getText().toString());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/settemperatura?temperatura=" + numero;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    tvRespuesta_PantallaConnection.setText("Response is: "+ response);
                },
                error -> {
                    tvRespuesta_PantallaConnection.setText("That didn't work!");
                    System.out.println(error.networkResponse);
                }
        );

        queue.add(stringRequest);
    }

    public void bajarTemperatura(View view) {
        tvRespuesta_PantallaConnection.setText("");
        tempDeseada-=0.5;
        tempDeseadaString = Double.toString(tempDeseada);
        etNumero_PantallaConnection.setText(tempDeseadaString);
    }

    public void subirTemperatura(View view) {
        tvRespuesta_PantallaConnection.setText("");
        tempDeseada+=0.5;
        tempDeseadaString = Double.toString(tempDeseada);
        etNumero_PantallaConnection.setText(tempDeseadaString);
    }

    public void ActualizarTemp(View view) {
        tvRespuesta_PantallaConnection.setText("");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/gettemperatura";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if(response.equals("nan")){
                        tvTempActual_PantallaConnection.setText("Temperatura actual: ERROR");
                    }
                    else{
                        tvTempActual_PantallaConnection.setText("Temperatura actual: " + response);
                    }
                },
                error -> {
                    tvRespuesta_PantallaConnection.setText("Error al actualizar la temperatura!");
                    System.out.println(error.networkResponse);
                }
        );

        queue.add(stringRequest);
    }
}
