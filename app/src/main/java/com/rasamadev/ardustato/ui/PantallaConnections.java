package com.rasamadev.ardustato.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.Connection;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AdapterConnections;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class PantallaConnections extends AppCompatActivity{

    // INSTANCIA BASE DATOS
    private OperacionesBaseDatos datos;

    // ELEMENTOS PAGINA CONEXIONES
    private ViewPager pagerConnections;
    private ConstraintLayout connections;
    private ConstraintLayout perfil;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;

    // ELEMENTOS LISTA CONEXIONES
    private ListView lista;
    private List<Connection> connectionsList;
    private TextView tvNoHayConnections_PantallaConnections;
    private FloatingActionButton floatBtnCrearConnection_PantallaConnections;

    // ATRIBUTOS PERFIL
    private TextView tvFullname_Perfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantallaconnections);

        // FIND VIEW BY ID´s
        pagerConnections = findViewById(R.id.pagerConnections);
        bottomNav = findViewById(R.id.bottomNav);
        toolbar = findViewById(R.id.toolbar);

        // OBTENER INSTANCIA BBDD
        datos = OperacionesBaseDatos.obtenerInstancia(this);

        // ACTIVAMOS TOOLBAR
        setSupportActionBar(toolbar);

        // CONTROL BOTONES "BOTTOMNAVIGATIONBAR"
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.itConnections){
                    pagerConnections.setCurrentItem(0);
                }
                else{
                    pagerConnections.setCurrentItem(1);
                }
                return true;
            }
        });

        // CONTROL DEL BOTON DE ATRAS
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // SALIR DE LA APP
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);

        // VIEWPAGER PARA LAS PAGINAS DE CONEXIONES Y PERFIL
        pagerConnections.setAdapter(new PagerAdapter(){
            @Override
            public int getCount() {
                return 2;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup collection, int position) {
                View paginaactual = null;
                switch (position){
                    case 0:
                        if(connections == null){
                            connections = (ConstraintLayout) LayoutInflater.from(PantallaConnections.this).inflate(R.layout.connections, null);
                            cargarConnections();
                        }
                        paginaactual = connections;
                        break;
                    case 1:
                        if(perfil == null){
                            perfil = (ConstraintLayout) LayoutInflater.from(PantallaConnections.this).inflate(R.layout.perfil, null);
                            cargarPerfil();
                        }
                        paginaactual = perfil;
                        break;
                }
                ViewPager vp = (ViewPager) collection;
                vp.addView(paginaactual, 0);
                return paginaactual;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
                ((ViewPager) collection).removeView((View) view);
            }
        });

        // REGISTRAR CAMBIOS PAGINAS
        pagerConnections.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        bottomNav.setSelectedItemId(R.id.itConnections);
                        break;
                    case 1:
                        bottomNav.setSelectedItemId(R.id.itPerfil);
                        break;
                }
                // super.onPageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // MOSTRAR ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    // OPCIONES MENU ACTIONBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.acercade_menu_actionbar){
            AlertDialogsUtil.mostrarMensaje(this,"ACERCA DE","Aplicacion desarrollada por Raúl Sastre Martin, con la colaboracion de Juan Carlos Sastre Gómez respecto a la parte de Arduino.\n\nGithub: https://github.com/rasamadev/Ardustato");
        }

        return super.onOptionsItemSelected(item);
    }

    private void cargarConnections(){
        lista = connections.findViewById(R.id.listViewConnections);
        tvNoHayConnections_PantallaConnections = connections.findViewById(R.id.tvNoHayConnections_PantallaConnections);
        floatBtnCrearConnection_PantallaConnections = connections.findViewById(R.id.floatBtnCrearConnection_PantallaConnections);

        // HACEMOS UNA CONSULTA DE LAS CONEXIONES ASIGNADAS AL USUARIO INICIADO
        // Y LAS CARGAMOS EN LA LISTVIEW
        connectionsList = datos.selectConnectionsByUser(PantallaIniciarSesion.ID_USER);
        if(connectionsList.size() == 0){
            lista.setVisibility(View.INVISIBLE);
            tvNoHayConnections_PantallaConnections.setVisibility(View.VISIBLE);
        }
        else{
            lista.setVisibility(View.VISIBLE);
            tvNoHayConnections_PantallaConnections.setVisibility(View.INVISIBLE);

            AdapterConnections adapterconnections = new AdapterConnections(this, R.layout.connection_item, connectionsList);
            lista.setAdapter(adapterconnections);
        }

        // AL PULSAR EN UNA CONEXION DE LA LISTA
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O) // LO REQUIERE "LocalDateTime"
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ip = connectionsList.get(position).getIp();

                // RECOGEMOS LA FECHA Y HORA DEL SISTEMA
                LocalDateTime localDate = LocalDateTime.now();
                int hora  = localDate.getHour();
                int minutos = localDate.getMinute();
                int segundos = localDate.getSecond();
                int dia = localDate.getDayOfMonth();
                int mes = localDate.getMonthValue();
                int anyo = localDate.getYear();

                RequestQueue queue = Volley.newRequestQueue(PantallaConnections.this);
                String url = "http://" + ip + "/firstconnection?hora=" + hora + "&minutos=" + minutos + "&segundos=" + segundos + "&dia=" + dia + "&mes=" + mes + "&anyo=" + anyo;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String tempActual = jsonObject.getString("tempActual");
                            String tempDeseada = jsonObject.getString("tempDeseada");
                            boolean stateProgramador;
                            if(jsonObject.getString("stateProgramador").equals("0")){
                                stateProgramador = false;
                            }
                            else{
                                stateProgramador = true;
                            }
                            String horaProgramador = jsonObject.getString("horaProgramador");
                            String minutoProgramador = jsonObject.getString("minutoProgramador");
                            String tempProgramador = jsonObject.getString("tempProgramador");

                            Intent i = new Intent(PantallaConnections.this, PantallaConnection.class);
                            i.putExtra("ip",ip);
                            i.putExtra("tempActual",tempActual);
                            i.putExtra("tempDeseada",tempDeseada);
                            i.putExtra("stateProgramador",stateProgramador);
                            i.putExtra("horaProgramador",horaProgramador);
                            i.putExtra("minutoProgramador",minutoProgramador);
                            i.putExtra("tempProgramador",tempProgramador);
                            startActivity(i);
                        }
                        catch(JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error -> {
                        AlertDialogsUtil.mostrarMensaje(PantallaConnections.this,"ERROR","No se ha podido conectar a la IP: " + ip + ".\nCAUSA: " + error);
                    });

                queue.add(stringRequest);
            }
        });

        // AL MANTENER PULSADO EN UNA CONEXION DE LA LISTA
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String idConnection = connectionsList.get(position).getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(PantallaConnections.this);
                builder.setTitle("CONFIRMACION");
                builder.setMessage("¿Estas seguro de borrar esta conexion? Una vez haya sido borrada, no se podra recuperar.");
                builder.setCancelable(false);
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datos.deleteConnectionById(idConnection);
                        Toast.makeText(PantallaConnections.this, "Conexion borrada con exito", Toast.LENGTH_SHORT).show();
                        cargarConnections();
                    }
                });
                builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ESTE BOTON SIMPLEMENTE CIERRA EL ALERTDIALOG
                    }
                });
                builder.create();
                builder.show();

                return true;
            }
        });
    }

    private void cargarPerfil(){
        tvFullname_Perfil = perfil.findViewById(R.id.tvFullname_Perfil);
        tvFullname_Perfil.setText(PantallaIniciarSesion.FULLNAME_USER);
    }

    // BOTON FLOTANTE, LLEVA A LA PANTALLA PARA CREAR UNA NUEVA CONEXION
    public void PantallaCrearConnection(View view) {
        Intent i = new Intent(this,PantallaCrearConnection.class);
        startActivity(i);
    }

    public void PantallaModificarPerfil(View view) {
        Intent i = new Intent(this,PantallaModificarPerfil.class);
        startActivity(i);
    }

    public void PantallaCambiarPass(View view) {
        Intent i = new Intent(this,PantallaModificarPass.class);
        startActivity(i);
    }

    public void eliminarCuenta(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaConnections.this);
        builder.setTitle("CUIDADO");
        builder.setMessage("Estas a punto de borrar tu cuenta, ¿estas seguro? Una vez confirmada la operacion, no se puede revertir.");
        builder.setCancelable(false);
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datos.deleteUserById(PantallaIniciarSesion.ID_USER);
                Toast.makeText(PantallaConnections.this, "Usuario borrado con exito.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PantallaConnections.this,PantallaInicio.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    public void cerrarSesion(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PantallaConnections.this);
        builder.setTitle("CERRAR SESION");
        builder.setMessage("¿Quieres cerrar sesion?");
        builder.setCancelable(false);
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PantallaConnections.this, "Has cerrado sesion.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PantallaConnections.this,PantallaInicio.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }
}