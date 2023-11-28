package com.example.proyecto_agz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import static com.example.proyecto_agz.LoginActivity.*;

public class MainActivity extends AppCompatActivity {

    private TextView tvTexto;
    private LinearLayout llListado;
    private Button btCrearAnuncio;
    LinkedList<Anuncio> listAnuncios = new LinkedList<>();
    LinkedList<BancoHoras> listBanco = new LinkedList<>();
    LinkedList<Sala> listSalas = new LinkedList<>();
    LinkedList<Tarea> listTareas = new LinkedList<>();

    ImageView ivHome;

    private Button btActividades;
    private Button btBancoHoras;
    private Button btSalas;
    private Button btTareas;
    private Button btTareasMayores;
    private Button btTareasMenores;


    public final static String KEY_EDITAR = "";
    public final static String KEY_CADENA = "kc";
    public final static String TITULO_EDITAR = "TITULO_EDITAR";
    public final static String DESCRIPCION_EDITAR = "DESCRIPCION_EDITAR";
    public final static String NOMBRE_EDITAR = "NOMBRE_EDITAR";
    public final static String HORAS_EDITAR = "HORAS_EDITAR";
    public final static String FECHA_INICIO_EDITAR = "FECHA_INICIO_EDITAR";
    public final static String FECHA_FIN_EDITAR = "FECHA_FIN_EDITAR";
    public final static String TEXTO_EDITAR = "TEXTO_EDITAR";
    public final static String TIPO_EDITAR = "TIPO_EDITAR";
    public final static String ESTADO_EDITAR = "ESTADO_EDITAR";
    public final int REQUEST_CODE_EDITAR = 1;


    private final static int ID_ACTIVIDADES = 0;
    private final static int ID_BANCO = 1;
    private final static int ID_SALAS = 2;
    private final static int ID_TAREAS = 3;
    private final static int ID_ANUNCIOS = 4;
    //Boton seleccionado
    private int idButton = ID_ANUNCIOS;
    private Button[] array_buttons;

    private int[] array_drawables;

    int idUsuario;
    final int idEditar = -1;
    private final static int ID_SIMPA = 0;
    private final static int ID_ADMIN = 2;

    String[] rangos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getActionBar().hide();

        ivHome = findViewById(R.id.ivHome);
        ivHome.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (idButton != ID_ANUNCIOS) {

                    Button button = array_buttons[idButton];
                    button.setEnabled(true);
                    idButton = ID_ANUNCIOS;
                    listarAnuncios("https://sombrous-livers.000webhostapp.com/select_anuncio_agz.php");
                }
            }
        });

        tvTexto = findViewById(R.id.tvTexto);
        llListado = findViewById(R.id.llAnuncios);
        btCrearAnuncio = findViewById(R.id.btCrearAnuncio);

        btActividades = findViewById(R.id.btActividades);
        btBancoHoras = findViewById(R.id.btBancoHoras);
        btSalas = findViewById(R.id.btSalas);
        btTareas = findViewById(R.id.btTareas);

        btTareasMayores = findViewById(R.id.btTareasMayores);
        btTareasMenores = findViewById(R.id.btTareasMenores);
        btTareasMayores.setVisibility(View.GONE);
        btTareasMenores.setVisibility(View.GONE);

        array_buttons = new Button[]{btActividades, btBancoHoras, btSalas, btTareas};
        array_drawables = new int[]{R.drawable.bt_redondo_tarea_rojo, R.drawable.bt_redondo_tarea_amarillo, R.drawable.bt_redondo_tarea_verde};

        rangos = getResources().getStringArray(R.array.usuarios);

        Intent intent = getIntent();
        idUsuario = intent.getIntExtra(KEY_USUARIO, -1);

        if (idUsuario != ID_ADMIN){

            btCrearAnuncio.setVisibility(View.GONE);
        }

        if (idUsuario == ID_SIMPA){

            btBancoHoras.setEnabled(false);
            btSalas.setEnabled(false);
            btTareas.setEnabled(false);
        }

        String textoBienvenida = (String) tvTexto.getText();
        tvTexto.setText(textoBienvenida + " " + rangos[idUsuario].toLowerCase() + "!");

        listAnuncios.clear();
        listBanco.clear();
        listSalas.clear();
        listTareas.clear();
        listarAnuncios("https://sombrous-livers.000webhostapp.com/select_anuncio_agz.php");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listarAnuncios("https://sombrous-livers.000webhostapp.com/select_anuncio_agz.php");
    }

    private void listarAnuncios(String URL) {

        llListado.removeAllViews();
        listAnuncios.clear();

        HttpsTrustManager.allowAllSSL();

        //String URL = "https://192.168.1.46/PHP/select_anuncio_agz.php";

        Log.d("XYZ", URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Anuncio anuncio = new Anuncio();

                        anuncio.setIdAnuncio(Integer.parseInt(jsonObject.getString("id")));
                        anuncio.setTituloAnuncio(jsonObject.getString("titulo"));
                        anuncio.setDescripcionAnuncio(jsonObject.getString("descripcion"));
                        listAnuncios.add(anuncio);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mostarAnuncios();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("XYZ", error + "");
                error.printStackTrace();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void mostarAnuncios (){

        RelativeLayout relativeLayout;
        LayoutInflater layoutInflater = getLayoutInflater();

        TextView tvTitulo;
        TextView tvDescripcion;
        ImageView ivEditar;
        ImageView ivEliminar;

        llListado.removeAllViews();
        Log.d("XYZ", listAnuncios.size() + "");
        for (Anuncio anuncio1: listAnuncios) {

            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.item_anuncio, null);

            tvTitulo = relativeLayout.findViewById(R.id.tv_titulo);
            tvTitulo.setText(anuncio1.getTituloAnuncio());

            tvDescripcion = relativeLayout.findViewById(R.id.tv_descripcion);
            tvDescripcion.setText(anuncio1.getDescripcionAnuncio());

            ivEditar = relativeLayout.findViewById(R.id.ivEditar);
            ivEditar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Log.d("XYZ", "Editar: " + anuncio1.getIdAnuncio());
                    editarAnuncio(anuncio1.getIdAnuncio(), anuncio1.getTituloAnuncio(), anuncio1.getDescripcionAnuncio());
                }
            });

            ivEliminar = relativeLayout.findViewById(R.id.ivEliminar);
            ivEliminar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    eliminarElemento(anuncio1.getIdAnuncio());
                    //eliminarAnuncio(anuncio1.getIdAnuncio());
                }
            });

            if (idUsuario != ID_ADMIN){

                ivEditar.setVisibility(View.INVISIBLE);
                ivEliminar.setVisibility(View.INVISIBLE);
            }

            llListado.addView(relativeLayout);
        }
    }

    public void editarAnuncio(int id, String titulo, String descripcion) {

        if (idButton < ID_ANUNCIOS) {

            Log.d("XYZ", "Editar: " + id);
            Intent intent = new Intent(this, CreadorActividadActivity.class);
            intent.putExtra(KEY_EDITAR, id + "");
            intent.putExtra(TITULO_EDITAR, titulo);
            intent.putExtra(DESCRIPCION_EDITAR, descripcion);
            startActivityForResult(intent, REQUEST_CODE_EDITAR);
        } else {

            Log.d("XYZ", "Editar: " + id);
            Intent intent = new Intent(this, CreadorAnuncioActivity.class);
            intent.putExtra(KEY_EDITAR, id + "");
            intent.putExtra(TITULO_EDITAR, titulo);
            intent.putExtra(DESCRIPCION_EDITAR, descripcion);
            startActivityForResult(intent, REQUEST_CODE_EDITAR);
        }

    }

    public void eliminarAnuncio(int id) {

        HttpsTrustManager.allowAllSSL();

        String url = "";

        if (idButton < ID_ANUNCIOS) {

            url = "https://sombrous-livers.000webhostapp.com/eliminar_actividad_agz.php";
        } else {

            url = "https://sombrous-livers.000webhostapp.com/eliminar_anuncio_agz.php";
        }

        String URL = Uri
                .parse(url)
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(id))
                .build()
                .toString();

        Log.d("XYZ", URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Si procesa o no procesa
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), R.string.eliminado_correctamente, Toast.LENGTH_SHORT).show();

                //Se reinicia el listado
                llListado.removeAllViews();
                listAnuncios.clear();
                if (idButton < ID_ANUNCIOS) {

                    listarAnuncios("https://sombrous-livers.000webhostapp.com/select_actividades_agz.php");
                } else {

                    Toast.makeText(getApplicationContext(), R.string.anuncio_eliminado_correctamente, Toast.LENGTH_SHORT).show();
                    listarAnuncios("https://sombrous-livers.000webhostapp.com/select_anuncio_agz.php");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }

    public void onClickCrearAnuncio(View view) {

        switch (idButton) {
            case 0:

                Intent intent = new Intent(this, CreadorActividadActivity.class);
                intent.putExtra(KEY_EDITAR, idEditar + "");
                startActivityForResult(intent, REQUEST_CODE_EDITAR);
                break;
            case 1:

                Intent intent1 = new Intent(this, CreadorBancoHorasActivity.class);
                intent1.putExtra(KEY_EDITAR, idEditar + "");
                startActivityForResult(intent1, REQUEST_CODE_EDITAR);
                break;
            case 2:

                Intent intent2 = new Intent(this, CreadorSalaActivity.class);
                intent2.putExtra(KEY_EDITAR, idEditar + "");
                startActivityForResult(intent2, REQUEST_CODE_EDITAR);
                break;
            case 3:

                Intent intent3 = new Intent(this, CreadorTareaActivity.class);
                intent3.putExtra(KEY_EDITAR, idEditar + "");
                startActivityForResult(intent3, REQUEST_CODE_EDITAR);
                break;
            case 4:

                Intent intent4 = new Intent(this, CreadorAnuncioActivity.class);
                intent4.putExtra(KEY_EDITAR, idEditar + "");
                startActivityForResult(intent4, REQUEST_CODE_EDITAR);
                break;
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDITAR && resultCode == RESULT_OK){
            String mensaje = data.getStringExtra(KEY_CADENA);

            switch (idButton) {
                case 0:

                    //Se reinicia el listado
                    llListado.removeAllViews();
                    listAnuncios.clear();
                    listarAnuncios("https://sombrous-livers.000webhostapp.com/select_actividades_agz.php");
                    break;
                case 1:

                    //Se reinicia el listado
                    llListado.removeAllViews();
                    listBanco.clear();
                    listarBancoHoras("https://sombrous-livers.000webhostapp.com/select_banco_agz.php");
                    break;
                case 2:

                    //Se reinicia el listado
                    llListado.removeAllViews();
                    listSalas.clear();
                    listarSalas("https://sombrous-livers.000webhostapp.com/select_salas_agz.php");
                    break;
                case 3:

                    //Se reinicia el listado
                    llListado.removeAllViews();
                    listTareas.clear();
                    listarTareas("https://sombrous-livers.000webhostapp.com/select_tareas_mayores_agz.php");
                    break;
                case 4:

                    //Se reinicia el listado
                    llListado.removeAllViews();
                    listAnuncios.clear();
                    listarAnuncios("https://sombrous-livers.000webhostapp.com/select_anuncio_agz.php");
                    break;
            }
        }
    }


    //----------------ACTIVIDADES----------------------------------------------

    public void onClickRellenarActividades(View view) {

        btCrearAnuncio.setText(R.string.crear_actividad);
        listarAnuncios("https://sombrous-livers.000webhostapp.com/select_actividades_agz.php");

        //Se habilita el antiguo y se deshabilita el nuevo
        Button button;
        if (idButton < ID_ANUNCIOS){

            button = array_buttons[idButton];
            button.setEnabled(true);
        }
        idButton = ID_ACTIVIDADES;
        button = array_buttons[idButton];
        button.setEnabled(false);

        btTareasMayores.setVisibility(View.GONE);
        btTareasMenores.setVisibility(View.GONE);
    }

    //----------------BANCO-DE-HORAS----------------------------------------------

    public void onClickRellenarBanco(View view) {

        btCrearAnuncio.setText(R.string.banco_horas);
        listarBancoHoras("https://sombrous-livers.000webhostapp.com/select_banco_agz.php");

        //Se habilita el antiguo y se deshabilita el nuevo
        Button button;
        if (idButton < ID_ANUNCIOS){

            button = array_buttons[idButton];
            button.setEnabled(true);
        }
        idButton = ID_BANCO;
        button = array_buttons[idButton];
        button.setEnabled(false);

        btTareasMayores.setVisibility(View.GONE);
        btTareasMenores.setVisibility(View.GONE);
    }

    private void listarBancoHoras(String URL) {

        llListado.removeAllViews();
        listBanco.clear();

        HttpsTrustManager.allowAllSSL();

        //String URL = "https://192.168.1.46/PHP/select_anuncio_agz.php";

        Log.d("XYZ", URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        BancoHoras bancoHoras = new BancoHoras();

                        bancoHoras.setIdBanco(Integer.parseInt(jsonObject.getString("id")));
                        bancoHoras.setNombreBanco(jsonObject.getString("nombre"));
                        bancoHoras.setHorasBanco(Float.parseFloat(jsonObject.getString("horas")));
                        Log.d("XYZ", "Float: " + Float.parseFloat(jsonObject.getString("horas")));
                        Log.d("XYZ", "Float: " + bancoHoras.getHorasBanco());
                        listBanco.add(bancoHoras);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mostarBanco();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("XYZ", error + "");
                error.printStackTrace();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void mostarBanco() {

        RelativeLayout relativeLayout;
        LayoutInflater layoutInflater = getLayoutInflater();

        TextView tvNombre;
        TextView tvHoras;
        ImageView ivEditar;
        ImageView ivEliminar;

        llListado.removeAllViews();
        Log.d("XYZ", listBanco.size() + "");
        for (BancoHoras bancoHoras1: listBanco) {

            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.item_banco_horas, null);

            tvNombre = relativeLayout.findViewById(R.id.tvNombre);
            tvNombre.setText(bancoHoras1.getNombreBanco());

            tvHoras = relativeLayout.findViewById(R.id.tvHoras);
            tvHoras.setText(bancoHoras1.getHorasBanco() + " horas");
            Log.d("XYZ", "Floating: " + bancoHoras1.getHorasBanco());

            ivEditar = relativeLayout.findViewById(R.id.ivEditar);
            ivEditar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Log.d("XYZ", "Editar: " + bancoHoras1.getIdBanco());
                    editarBanco(bancoHoras1.getIdBanco(), bancoHoras1.getNombreBanco(), bancoHoras1.getHorasBanco());
                }
            });

            ivEliminar = relativeLayout.findViewById(R.id.ivEliminar);
            ivEliminar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    eliminarElemento(bancoHoras1.getIdBanco());
                    //eliminarBanco(bancoHoras1.getIdBanco());
                }
            });

            if (idUsuario != ID_ADMIN){

                ivEditar.setVisibility(View.INVISIBLE);
                ivEliminar.setVisibility(View.INVISIBLE);
            }

            llListado.addView(relativeLayout);
        }
    }

    public void editarBanco(int id, String nombre, float horas) {

        Log.d("XYZ", "Editar: " + id);
        Intent intent = new Intent(this, CreadorBancoHorasActivity.class);
        intent.putExtra(KEY_EDITAR, id + "");
        intent.putExtra(NOMBRE_EDITAR, nombre);
        intent.putExtra(HORAS_EDITAR, horas + "");
        Log.d("XYZ", "Editar: " + nombre + horas);
        startActivityForResult(intent, REQUEST_CODE_EDITAR);
    }

    public void eliminarBanco(int id) {

        HttpsTrustManager.allowAllSSL();

        String url = "https://sombrous-livers.000webhostapp.com/eliminar_banco_agz.php";

        String URL = Uri
                .parse(url)
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(id))
                .build()
                .toString();

        Log.d("XYZ", URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Si procesa o no procesa
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), R.string.eliminado_correctamente, Toast.LENGTH_SHORT).show();

                //Se reinicia el listado
                llListado.removeAllViews();
                listBanco.clear();
                listarBancoHoras("https://sombrous-livers.000webhostapp.com/select_banco_agz.php");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }

    //----------------SALAS----------------------------------------------

    public void onClickRellenarSalas(View view) {

        btCrearAnuncio.setText(R.string.reservar_sala);
        listarSalas("https://sombrous-livers.000webhostapp.com/select_salas_agz.php");

        //Se habilita el antiguo y se deshabilita el nuevo
        Button button;
        if (idButton < ID_ANUNCIOS){

            button = array_buttons[idButton];
            button.setEnabled(true);
        }
        idButton = ID_SALAS;
        button = array_buttons[idButton];
        button.setEnabled(false);

        btTareasMayores.setVisibility(View.GONE);
        btTareasMenores.setVisibility(View.GONE);
    }

    private void listarSalas(String URL) {

        llListado.removeAllViews();
        listSalas.clear();

        HttpsTrustManager.allowAllSSL();

        Log.d("XYZ", URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Sala sala = new Sala();

                        sala.setIdSala(Integer.parseInt(jsonObject.getString("id")));
                        sala.setNombreSala(jsonObject.getString("nombre"));
                        sala.setFechaInicioSala(jsonObject.getString("fecha_inicio"));
                        sala.setFechaFinalSala(jsonObject.getString("fecha_fin"));
                        listSalas.add(sala);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mostarSalas();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("XYZ", error + "");
                error.printStackTrace();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void mostarSalas() {

        RelativeLayout relativeLayout;
        LayoutInflater layoutInflater = getLayoutInflater();

        TextView tvNombre;
        TextView tvFechaInicio;
        TextView tvFechaFin;
        ImageView ivEditar;
        ImageView ivEliminar;

        Log.d("XYZ", listSalas.size() + "");
        llListado.removeAllViews();
        for (Sala salas1: listSalas) {

            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.item_sala, null);

            tvNombre = relativeLayout.findViewById(R.id.tvNombre);
            tvNombre.setText(salas1.getNombreSala());

            tvFechaInicio = relativeLayout.findViewById(R.id.tvFechaInicio);
            tvFechaInicio.setText(salas1.getFechaInicioSala() + "");

            tvFechaFin = relativeLayout.findViewById(R.id.tvFechaFin);
            tvFechaFin.setText(salas1.getFechaFinalSala() + "");

            ivEditar = relativeLayout.findViewById(R.id.ivEditar);
            ivEditar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    editarSala(salas1.getIdSala(), salas1.getNombreSala(), salas1.getFechaInicioSala(), salas1.getFechaFinalSala());
                }
            });

            ivEliminar = relativeLayout.findViewById(R.id.ivEliminar);
            ivEliminar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    eliminarElemento(salas1.getIdSala());
                    //eliminarSala(salas1.getIdSala());
                }
            });

            if (idUsuario != ID_ADMIN){

                ivEditar.setVisibility(View.INVISIBLE);
                ivEliminar.setVisibility(View.INVISIBLE);
            }

            llListado.addView(relativeLayout);
        }
    }

    public void editarSala(int id, String nombre, String fecha_inicio, String fecha_fin) {

        Log.d("XYZ", "Editar: " + id);
        Intent intent = new Intent(this, CreadorSalaActivity.class);
        intent.putExtra(KEY_EDITAR, id + "");
        intent.putExtra(NOMBRE_EDITAR, nombre);
        intent.putExtra(FECHA_INICIO_EDITAR, fecha_inicio);
        intent.putExtra(FECHA_FIN_EDITAR, fecha_fin);
        startActivityForResult(intent, REQUEST_CODE_EDITAR);
    }

    public void eliminarSala(int id) {

        HttpsTrustManager.allowAllSSL();

        String url = "https://sombrous-livers.000webhostapp.com/eliminar_sala_agz.php";

        String URL = Uri
                .parse(url)
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(id))
                .build()
                .toString();

        Log.d("XYZ", URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Si procesa o no procesa
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), R.string.eliminado_correctamente, Toast.LENGTH_SHORT).show();

                //Se reinicia el listado
                llListado.removeAllViews();
                listSalas.clear();
                listarSalas("https://sombrous-livers.000webhostapp.com/select_salas_agz.php");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }


    //----------------TAREAS----------------------------------------------

    public void onClickRellenarTareasMayores(View view) {

        btTareasMayores.setVisibility(View.VISIBLE);
        btTareasMenores.setVisibility(View.VISIBLE);

        btCrearAnuncio.setText(R.string.crear_tarea);

        llListado.removeAllViews();
        listTareas.clear();
        listarTareas("https://sombrous-livers.000webhostapp.com/select_tareas_mayores_agz.php");

        //Se habilita el antiguo y se deshabilita el nuevo
        Button button;
        if (idButton < ID_ANUNCIOS){

            button = array_buttons[idButton];
            button.setEnabled(true);
        }
        idButton = ID_TAREAS;
        button = array_buttons[idButton];
        button.setEnabled(false);
        //Se deshabilita el btTareasMayores
        btTareasMayores.setEnabled(false);
        btTareasMenores.setEnabled(true);
    }

    public void onClickRellenarTareasMenores(View view) {

        btCrearAnuncio.setText(R.string.crear_tarea);

        llListado.removeAllViews();
        listTareas.clear();
        listarTareas("https://sombrous-livers.000webhostapp.com/select_tareas_menores_agz.php");

        //Se deshabilita el btTareasMayores
        btTareasMayores.setEnabled(true);
        btTareasMenores.setEnabled(false);
    }


    private void listarTareas(String URL) {

        llListado.removeAllViews();
        listTareas.clear();

        HttpsTrustManager.allowAllSSL();

        Log.d("XYZ", URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Tarea tarea = new Tarea();

                        tarea.setIdTarea(Integer.parseInt(jsonObject.getString("id")));
                        tarea.setTextoTarea(jsonObject.getString("texto"));
                        tarea.setTipoTarea(Integer.parseInt(jsonObject.getString("tipo")));
                        tarea.setEstadoTarea(Integer.parseInt(jsonObject.getString("estado")));
                        listTareas.add(tarea);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mostarTareas();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("XYZ", error + "");
                error.printStackTrace();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void mostarTareas() {

        RelativeLayout relativeLayout;
        LayoutInflater layoutInflater = getLayoutInflater();

        TextView tvTexto;
        ImageView ivEditar;
        ImageView ivEliminar;

        Log.d("XYZ", listTareas.size() + "");
        llListado.removeAllViews();
        for (Tarea tareas1: listTareas) {

            relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.item_tarea, null);

            tvTexto = relativeLayout.findViewById(R.id.tvTexto);
            tvTexto.setText(tareas1.getTextoTarea());

            relativeLayout.setBackgroundResource(array_drawables[tareas1.getEstadoTarea()]);

            ivEditar = relativeLayout.findViewById(R.id.ivEditar);
            ivEditar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    editarTarea(tareas1.getIdTarea(), tareas1.getTextoTarea(), tareas1.getTipoTarea(), tareas1.getEstadoTarea());
                }
            });

            ivEliminar = relativeLayout.findViewById(R.id.ivEliminar);
            ivEliminar.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    eliminarElemento(tareas1.getIdTarea());
                    //eliminarTarea(tareas1.getIdTarea());
                }
            });

            if (idUsuario != ID_ADMIN){

                ivEditar.setVisibility(View.INVISIBLE);
                ivEliminar.setVisibility(View.INVISIBLE);
            }

            llListado.addView(relativeLayout);
        }
    }

    public void editarTarea(int id, String texto, int tipo, int estado) {

        Log.d("XYZ", "Editar: " + id);
        Intent intent = new Intent(this, CreadorTareaActivity.class);
        intent.putExtra(KEY_EDITAR, id + "");
        intent.putExtra(TEXTO_EDITAR, texto);
        intent.putExtra(TIPO_EDITAR, tipo + "");
        intent.putExtra(ESTADO_EDITAR, estado + "");
        startActivityForResult(intent, REQUEST_CODE_EDITAR);
    }

    public void eliminarTarea(int id) {

        HttpsTrustManager.allowAllSSL();

        String url = "https://sombrous-livers.000webhostapp.com/eliminar_tarea_agz.php";

        String URL = Uri
                .parse(url)
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(id))
                .build()
                .toString();

        Log.d("XYZ", URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Si procesa o no procesa
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), R.string.eliminado_correctamente, Toast.LENGTH_SHORT).show();

                //Se reinicia el listado
                llListado.removeAllViews();
                listTareas.clear();
                if (btTareasMayores.isEnabled()){

                    llListado.removeAllViews();
                    listTareas.clear();
                    listarTareas("https://sombrous-livers.000webhostapp.com/select_tareas_menores_agz.php");
                } else {

                    llListado.removeAllViews();
                    listTareas.clear();
                    listarTareas("https://sombrous-livers.000webhostapp.com/select_tareas_mayores_agz.php");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }

    public void eliminarElemento(int id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.eliminar_elemento);
        builder.setPositiveButton(R.string.si, (dialog, which) -> {

            switch (idButton) {
                case 0:

                    eliminarAnuncio(id);
                    break;
                case 1:

                    eliminarBanco(id);
                    break;
                case 2:

                    eliminarSala(id);
                    break;
                case 3:

                    eliminarTarea(id);
                    break;
                case 4:

                    eliminarAnuncio(id);
                    break;
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}