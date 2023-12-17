package com.rasamadev.ardustato.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rasamadev.ardustato.R;
import com.rasamadev.ardustato.models.Connection;
import com.rasamadev.ardustato.models.User;
import com.rasamadev.ardustato.sqlite.OperacionesBaseDatos;
import com.rasamadev.ardustato.utils.AdapterConnections;
import com.rasamadev.ardustato.utils.AlertDialogsUtil;

import java.util.ArrayList;
import java.util.List;

public class PantallaConnections extends AppCompatActivity implements AdapterView.OnItemClickListener{

    // ATRIBUTOS BBDD Y DATOS USUARIO SESION INICIADA
    private OperacionesBaseDatos datos;
    private String idUserSesionIniciada;
    private String fullnameUserSesionIniciada;
    private String mailUserSesionIniciada;
    private String passUserSesionIniciada;

    // ATRIBUTOS LISTA CONEXIONES
    private ListView lista;
    private List<Connection> connectionsList;
    private TextView tvNoHayConnections_PantallaConnections;

    // ATRIBUTOS PAGINA CONEXIONES
    private ViewPager pagerConnections;
    private ConstraintLayout connections;
    private ConstraintLayout perfil;
    private BottomNavigationView bottomNav;
    private Toolbar toolbar;

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

        // ACTIVAMOS TOOLBAR
        setSupportActionBar(toolbar);

        // RECUPERAMOS LOS DATOS DEL USUARIO QUE HA INICIADO SESION
        Bundle bundle = getIntent().getExtras();
        idUserSesionIniciada = bundle.getString("id");
        fullnameUserSesionIniciada = bundle.getString("fullname");
        mailUserSesionIniciada = bundle.getString("mail");
        passUserSesionIniciada = bundle.getString("pass");
//        AlertDialogsUtil.mostrarError(this,"ID: " + idUserSesionIniciada + ", NAME: " + fullnameUserSesionIniciada + ", MAIL: " + mailUserSesionIniciada + ", PASS: " + passUserSesionIniciada);

        // BOTTOM NAVIGATION BAR
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
                Toast.makeText(PantallaConnections.this, "CERRAR APLICACION", Toast.LENGTH_SHORT).show();
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
//                super.onPageSelected(position);
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

    private void cargarConnections(){
        lista = connections.findViewById(R.id.listViewConnections);
        tvNoHayConnections_PantallaConnections = connections.findViewById(R.id.tvNoHayConnections_PantallaConnections);

        datos = OperacionesBaseDatos.obtenerInstancia(this);

        // HACEMOS UNA CONSULTA DE LAS CONEXIONES ASIGNADAS AL USUARIO INICIADO
        // Y LAS CARGAMOS EN LA LISTVIEW
        connectionsList = datos.selectConnectionsByUser(idUserSesionIniciada);
        if(connectionsList.size() == 0){
            tvNoHayConnections_PantallaConnections.setVisibility(View.VISIBLE);
        }
        else{
            tvNoHayConnections_PantallaConnections.setVisibility(View.INVISIBLE);

            AdapterConnections adapterconnections = new AdapterConnections(this, R.layout.connection_item, connectionsList);
            lista.setAdapter(adapterconnections);
            lista.setOnItemClickListener(this);
        }
    }

    private void cargarPerfil(){
        tvFullname_Perfil = perfil.findViewById(R.id.tvFullname_Perfil);
        tvFullname_Perfil.setText(fullnameUserSesionIniciada);
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
        // "EXPORTAMOS" EL ID DEL USUARIO QUE HA INICIADO SESION PARA QUE, CUANDO
        // SE CREEN CONEXIONES SE LES ASIGNEN A ESTE
        Intent i = new Intent(this,PantallaCrearConnection.class);
        i.putExtra("id",idUserSesionIniciada);
        startActivity(i);
    }

    public void modificarPerfil(View view) {
    }

    public void cambiarPass(View view) {
    }

    public void eliminarCuenta(View view) {
    }

    public void cerrarSesion(View view) {
    }
}