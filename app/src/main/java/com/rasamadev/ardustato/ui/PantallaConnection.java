package com.rasamadev.ardustato.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

public class PantallaConnection extends AppCompatActivity {

    // ELEMENTOS PANTALLA
    private EditText etNumero_PantallaConnection;
    private TextView tvTempActual_PantallaConnection;
    private TextView tvTempDeseada_PantallaConnection;

    private TextView tvProgramadorLabel_PantallaConnection;
    private MaterialSwitch switchProgramador_PantallaConnection;

    private TextView tvTempProgramadaLabel_PantallaConnection;
    private TextView tvTempProgramada_PantallaConnection;

    private TextView tvTempLabel_PantallaConnection;
    private ImageView btnMenosProgramador_PantallaConnection;
    private EditText etNumeroProgramador_PantallaConnection;
    private ImageView btnMasProgramador_PantallaConnection;

    private TextView tvHoraProgramadaLabel_PantallaConnection;
    private TextView tvHoraProgramada_PantallaConnection;

    private TextView tvHoraLabel_PantallaConnection;
    private SeekBar sbHora_PantallaConnection;
    private TextView tvIndicadorHora_PantallaConnection;

    private TextView tvMinutoLabel_PantallaConnection;
    private SeekBar sbMinuto_PantallaConnection;
    private TextView tvIndicadorMinuto_PantallaConnection;

    private Button btnProgramador_PantallaConnection;


    // ATRIBUTOS BUNDLE
    private String ip;                  // IP traspasada por bundle
    private String tempActual;          // Temperatura actual traspasada por bundle
    private String tempDeseada;         // Temperatura deseada traspasada por bundle
    private boolean stateProgramador;   // Estado del programador (activado o desactivado)
    private String horaProgramador;     // Ultima hora programada
    private String minutoProgramador;   // Ultimo minuto programado
    private String tempProgramador;     // Ultima temperatura programada

    // OTROS ATRIBUTOS
    private float tempDeseadaFloat;             // Temperatura deseada en valor float
    private float tempProgramadorFloat;         // Temperatura programada en valor float
    private String horaminutoProgramador;       // Cadena con la hora y minuto formateada en HH:MM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);

        // Recuperamos los extras traspasados de la clase "PantallaConnections"
        Bundle bundle = getIntent().getExtras();
        ip = bundle.getString("ip");
        tempActual = bundle.getString("tempActual");
        tempDeseada = bundle.getString("tempDeseada");
        stateProgramador = bundle.getBoolean("stateProgramador");
        horaProgramador = bundle.getString("horaProgramador");
        minutoProgramador = bundle.getString("minutoProgramador");
        tempProgramador = bundle.getString("tempProgramador");

        // FINDVIEWBYID´S
        etNumero_PantallaConnection = findViewById(R.id.etNumero_PantallaConnection);
        tvTempActual_PantallaConnection = findViewById(R.id.tvTempActual_PantallaConnection);
        tvTempDeseada_PantallaConnection = findViewById(R.id.tvTempDeseada_PantallaConnection);

        tvProgramadorLabel_PantallaConnection = findViewById(R.id.tvProgramadorLabel_PantallaConnection);
        switchProgramador_PantallaConnection = findViewById(R.id.switchProgramador_PantallaConnection);

        tvTempProgramadaLabel_PantallaConnection = findViewById(R.id.tvTempProgramadaLabel_PantallaConnection);
        tvTempProgramada_PantallaConnection = findViewById(R.id.tvTempProgramada_PantallaConnection);

        tvTempLabel_PantallaConnection = findViewById(R.id.tvTempLabel_PantallaConnection);
        btnMenosProgramador_PantallaConnection = findViewById(R.id.btnMenosProgramador_PantallaConnection);
        etNumeroProgramador_PantallaConnection = findViewById(R.id.etNumeroProgramador_PantallaConnection);
        btnMasProgramador_PantallaConnection = findViewById(R.id.btnMasProgramador_PantallaConnection);

        tvHoraProgramadaLabel_PantallaConnection = findViewById(R.id.tvHoraProgramadaLabel_PantallaConnection);
        tvHoraProgramada_PantallaConnection = findViewById(R.id.tvHoraProgramada_PantallaConnection);

        tvHoraLabel_PantallaConnection = findViewById(R.id.tvHoraLabel_PantallaConnection);
        sbHora_PantallaConnection = findViewById(R.id.sbHora_PantallaConnection);
        tvIndicadorHora_PantallaConnection = findViewById(R.id.tvIndicadorHora_PantallaConnection);

        tvMinutoLabel_PantallaConnection = findViewById(R.id.tvMinutoLabel_PantallaConnection);
        sbMinuto_PantallaConnection = findViewById(R.id.sbMinuto_PantallaConnection);
        tvIndicadorMinuto_PantallaConnection = findViewById(R.id.tvIndicadorMinuto_PantallaConnection);

        btnProgramador_PantallaConnection = findViewById(R.id.btnProgramador_PantallaConnection);

        // IMPEDIMOS QUE SE PUEDAN INSERTAR NUMEROS EN LOS EDITTEXT
        etNumero_PantallaConnection.setKeyListener(null);
        etNumeroProgramador_PantallaConnection.setKeyListener(null);

        // TEMPERATURA DESEADA Y PROGRAMADOR A FLOAT
        tempDeseadaFloat = Float.parseFloat(tempDeseada);
        tempProgramadorFloat = Float.parseFloat(tempProgramador);

        // ESTABLECEMOS LAS TEMPERATURAS EN LOS TEXTVIEW Y EDITTEXT DE LA PANTALLA
        etNumero_PantallaConnection.setText(tempDeseada);
        tvTempActual_PantallaConnection.setText(tempActual);
        tvTempDeseada_PantallaConnection.setText(tempDeseada);

        // --------------- PROGRAMADOR ---------------

        // METODO DEL SWITCH QUE EJECUTARA EL METODO "switchPulsado()" CADA VEZ QUE SE PULSE
        switchProgramador_PantallaConnection.setOnCheckedChangeListener((buttonView, isChecked) -> setStateProgramador(isChecked));

        // REGISTRO MOVIMIENTO SEEKBAR HORA
        sbHora_PantallaConnection.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 10){
                    tvIndicadorHora_PantallaConnection.setText("0" + progress);
                }
                else{
                    tvIndicadorHora_PantallaConnection.setText("" + progress);
                }
                checkChangeProgramador();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // NO SE USA
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // NO SE USA
            }
        });

        // REGISTRO MOVIMIENTO SEEKBAR MINUTO
        sbMinuto_PantallaConnection.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 10){
                    tvIndicadorMinuto_PantallaConnection.setText("0" + progress);
                }
                else{
                    tvIndicadorMinuto_PantallaConnection.setText("" + progress);
                }
                checkChangeProgramador();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // DESACTIVAMOS BOTON APLICAR CAMBIOS
        btnProgramador_PantallaConnection.setEnabled(false);

        // ESTABLECEMOS VALORES MAXIMOS DE LAS SEEKBAR
        sbHora_PantallaConnection.setMax(23);
        sbMinuto_PantallaConnection.setMax(59);

        // ESTABLECEMOS EL ESTADO DEL PROGRAMADOR SEGUN LA VARIABLE BOOLEANA QUE NOS ENVIE EL ARDUINO
        setStateProgramador(stateProgramador);

        // ESTABLECEMOS ULTIMA HORA, MINUTO Y TEMPERATURA PROGRAMADOS EN EL TEXVIEW, EDITTEXT Y LOS SEEKBAR
        tvTempProgramada_PantallaConnection.setText(tempProgramador);
        etNumeroProgramador_PantallaConnection.setText(tempProgramador);
        sbHora_PantallaConnection.setProgress(Integer.parseInt(horaProgramador));
        sbMinuto_PantallaConnection.setProgress(Integer.parseInt(minutoProgramador));

        // COMPROBACION DE SI LAS HORAS Y MINUTOS SON MENORES DE 10, PARA INCLUIRLES UN "0" DELANTE
        if(Integer.parseInt(horaProgramador) < 10){
            horaminutoProgramador = "0" + horaProgramador + ":";
        }
        else{
            horaminutoProgramador = "" + horaProgramador + ":";
        }
        if(Integer.parseInt(minutoProgramador) < 10){
            horaminutoProgramador += "0" + minutoProgramador;
        }
        else{
            horaminutoProgramador += minutoProgramador;
        }
        tvHoraProgramada_PantallaConnection.setText(horaminutoProgramador);
    }

    /**
     * BOTON DE "ENVIAR"
     * @param view
     */
    public void EnviarNumero(View view) {
        // PONEMOS EN EL CAMPO DE TEMPERATURA DESEADA LA TEMPERATURA QUE ACABAMOS DE PROGRAMAR
        tvTempDeseada_PantallaConnection.setText(etNumero_PantallaConnection.getText().toString());

        // CONVERTIMOS A FLOAT LA TEMPERATURA QUE ACABAMOS DE PROGRAMAR
        tempDeseadaFloat = Float.parseFloat(etNumero_PantallaConnection.getText().toString());

        // HTTP REQUEST PARA ESTABLECER LA TEMPERATURA DESEADA EN EL ARDUINO
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/settemperatura?temperatura=" + tempDeseadaFloat;

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

    /**
     * BOTON DE "-" PARA BAJAR 0.5 GRADOS LA TEMPERATURA
     * @param view
     */
    public void bajarTemperatura(View view) {
        if(tempDeseadaFloat > 0){
            tempDeseadaFloat-=0.50;
            tempDeseada = tempDeseadaFloat + "0";
            etNumero_PantallaConnection.setText(tempDeseada);
        }
    }

    /**
     * BOTON DE "+" PARA SUBIR 0.5 GRADOS LA TEMPERATURA
     * @param view
     */
    public void subirTemperatura(View view) {
        if(tempDeseadaFloat < 30){
            tempDeseadaFloat+=0.50;
            tempDeseada = tempDeseadaFloat + "0";
            etNumero_PantallaConnection.setText(tempDeseada);
        }
    }

    /**
     * BOTON DE "ACTUALIZAR TEMPERATURA ACTUAL" PARA "REFRESCAR" LA TEMPERATURA ACTUAL QUE DETECTE
     * EL ARDUINO
     * @param view
     */
    public void ActualizarTemp(View view) {
        // HTTP REQUEST PARA RECIBIR DEL ARDUINO LA TEMPERATURA ACTUAL
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

    public void bajarTemperaturaProgramador(View view) {
        if(tempProgramadorFloat > 0){
            tempProgramadorFloat-=0.50;
            tempProgramador = tempProgramadorFloat + "0";
            etNumeroProgramador_PantallaConnection.setText(tempProgramador);
        }
        checkChangeProgramador();
    }

    public void subirTemperaturaProgramador(View view) {
        if(tempProgramadorFloat < 30){
            tempProgramadorFloat+=0.50;
            tempProgramador = tempProgramadorFloat + "0";
            etNumeroProgramador_PantallaConnection.setText(tempProgramador);
        }
        checkChangeProgramador();
    }

    public void aplicarCambiosProgramador(View view) {
        boolean prog = switchProgramador_PantallaConnection.isChecked();
        String temp = etNumeroProgramador_PantallaConnection.getText().toString();
        int hora = sbHora_PantallaConnection.getProgress();
        int minuto = sbMinuto_PantallaConnection.getProgress();

        // ENVIAR SOLICITUD HTTP REQUEST PARA PROGRAMAR EL PROGRAMADOR (valga la redundancia)
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + "/setprogramador?stateprogramador=" + prog + "&tempprogramada=" + temp + "&horaprogramada=" + hora + "&minprogramado=" + minuto;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // CAMBIAR TEXTVIEW TEMPERATURA
                    tvTempProgramada_PantallaConnection.setText(temp);

                    // CAMBIAR TEXTVIEW HORA PROGRAMADA
                    if(hora < 10){
                        horaminutoProgramador = "0" + hora + ":";
                    }
                    else{
                        horaminutoProgramador = "" + hora + ":";
                    }

                    if(minuto < 10){
                        horaminutoProgramador += "0" + minuto;
                    }
                    else{
                        horaminutoProgramador += minuto;
                    }
                    tvHoraProgramada_PantallaConnection.setText(horaminutoProgramador);

                    // MODIFICAR VARIABLES BUNDLE
                    stateProgramador = prog;
                    tempProgramador = temp;
                    horaProgramador = Integer.toString(hora);
                    minutoProgramador = Integer.toString(minuto);

                    checkChangeProgramador();

                    // MOSTRAR ALERTDIALOG
                    AlertDialogsUtil.mostrarMensaje(this,"ENVIADO","Respuesta del servidor:\n" + response);
                },
                error -> {
                    Toast.makeText(this, "Error al configurar el programador! " + error, Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(stringRequest);
    }

    private void setStateProgramador(boolean b){
        // COMO HEMOS PULSADO EL SWITCH DEL PROGRAMADOR, TIENE QUE HACER LA COMPROBACION
        // PARA ACTIVAR O NO EL BOTON DE APLICAR CAMBIOS, INDEPENDIENTEMENTE DE QUE ESTE
        // METODO SIRVA PARA ACTIVAR O DESACTIVAR LOS ELEMENTOS
        checkChangeProgramador();

        if(b){
            tvProgramadorLabel_PantallaConnection.setText("Programador ON");
        }
        else{
            tvProgramadorLabel_PantallaConnection.setText("Programador OFF");
        }
        switchProgramador_PantallaConnection.setChecked(b);

        tvTempProgramadaLabel_PantallaConnection.setEnabled(b);
        tvTempProgramada_PantallaConnection.setEnabled(b);

        tvTempLabel_PantallaConnection.setEnabled(b);
        btnMenosProgramador_PantallaConnection.setEnabled(b);
        etNumeroProgramador_PantallaConnection.setEnabled(b);
        btnMasProgramador_PantallaConnection.setEnabled(b);

        tvHoraProgramadaLabel_PantallaConnection.setEnabled(b);
        tvHoraProgramada_PantallaConnection.setEnabled(b);

        tvHoraLabel_PantallaConnection.setEnabled(b);
        sbHora_PantallaConnection.setEnabled(b);
        tvIndicadorHora_PantallaConnection.setEnabled(b);

        tvMinutoLabel_PantallaConnection.setEnabled(b);
        sbMinuto_PantallaConnection.setEnabled(b);
        tvIndicadorMinuto_PantallaConnection.setEnabled(b);
    }

    private void checkChangeProgramador(){
        if(switchProgramador_PantallaConnection.isChecked() != stateProgramador
                || sbHora_PantallaConnection.getProgress() != Integer.parseInt(horaProgramador)
                || sbMinuto_PantallaConnection.getProgress() != Integer.parseInt(minutoProgramador)
                || !etNumeroProgramador_PantallaConnection.getText().toString().equals(tvTempProgramada_PantallaConnection.getText().toString())
        ){
            btnProgramador_PantallaConnection.setEnabled(true);
        }
        else{
            btnProgramador_PantallaConnection.setEnabled(false);
        }
    }
}
