package com.example.proyecto_agz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.proyecto_agz.LoginActivity.KEY_USUARIO;

public class CreadorTareaActivity extends AppCompatActivity {

    private TextView tvTexto;
    private EditText etTexto;

    private RadioGroup rgTipo;
    private RadioButton rbTareaMayor;
    private RadioButton rbTareaMenor;

    private RadioGroup rgEstado;
    private RadioButton rbPorHacer;
    private RadioButton rbEnProceso;
    private RadioButton rbHecho;

    private Button btCrearTarea;

    private Button[] array_btTipo;
    private Button[] array_btEstado;

    private final static int ID_TAREA_MAYOR = 0;
    private final static int ID_TAREA_MENOR = 1;

    private final static int ID_POR_HACER = 0;
    private final static int ID_EN_PROCESO = 1;
    private final static int ID_HECHO = 2;

    private final static int NO_ID = -1;

    public int idEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creador_tarea);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvTexto = findViewById(R.id.tvTexto);
        etTexto = findViewById(R.id.etTexto);

        rgTipo = findViewById(R.id.rgTipo);
        rbTareaMayor = findViewById(R.id.rbTareaMayor);
        rbTareaMayor.setChecked(true);
        rbTareaMenor = findViewById(R.id.rbTareaMenor);

        rgEstado = findViewById(R.id.rgEstado);
        rbPorHacer = findViewById(R.id.rbPorHacer);
        rbPorHacer.setChecked(true);
        rbEnProceso = findViewById(R.id.rbEnProceso);
        rbHecho = findViewById(R.id.rbHecho);

        btCrearTarea = findViewById(R.id.btCrearTarea);

        array_btTipo = new Button[]{rbTareaMayor, rbTareaMenor};
        array_btEstado = new Button[]{rbPorHacer, rbEnProceso, rbHecho};

        Intent intent = getIntent();
        idEditar = Integer.parseInt(intent.getStringExtra(MainActivity.KEY_EDITAR));

        if (idEditar != NO_ID) {

            //Cambiar Views
            String texto = intent.getStringExtra(MainActivity.TEXTO_EDITAR);
            int tipo = Integer.parseInt(intent.getStringExtra(MainActivity.TIPO_EDITAR));
            int estado = Integer.parseInt(intent.getStringExtra(MainActivity.ESTADO_EDITAR));

            etTexto.setText(texto);
            if (tipo == ID_TAREA_MAYOR){

                rbTareaMayor.setChecked(true);
            } else {

                rbTareaMenor.setChecked(true);
            }

            if (estado == ID_POR_HACER){

                rbPorHacer.setChecked(true);
            } else if (estado == ID_EN_PROCESO) {

                rbEnProceso.setChecked(true);
            } else {

                rbHecho.setChecked(true);
            }

            tvTexto.setText(R.string.editar_tarea);
            btCrearTarea.setText(R.string.editar_tarea);
        }
    }

    public void onClickCrearBanco(View view) {

        int textoLength = etTexto.getText().toString().trim().length();

        if (textoLength == 0){

            Toast.makeText(this, R.string.campos_sin_rellenar, Toast.LENGTH_SHORT).show();
        } else {

            if (idEditar != NO_ID) {

                ejecutarServicio(idEditar);
            } else {

                ejecutarServicio(NO_ID);
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.KEY_EDITAR, 2);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void ejecutarServicio(int id) {

        HttpsTrustManager.allowAllSSL();

        int iTipo;
        int iEstado;
        //RadioButton seleccionado de los RadioGroup
        int selectedId = rgTipo.getCheckedRadioButtonId();
        RadioButton rbSeleccionado = (RadioButton) findViewById(selectedId);

        if (rbSeleccionado == array_btTipo[ID_TAREA_MAYOR]) {

            iTipo = ID_TAREA_MAYOR;
        } else {

            iTipo = ID_TAREA_MENOR;
        }
        selectedId = rgEstado.getCheckedRadioButtonId();
        rbSeleccionado = (RadioButton) findViewById(selectedId);

        if (rbSeleccionado == array_btEstado[ID_POR_HACER]){

            iEstado = ID_POR_HACER;
        } else if (rbSeleccionado == array_btEstado[ID_EN_PROCESO]) {

            iEstado = ID_EN_PROCESO;
        } else {

            iEstado = ID_HECHO;
        }

        String URL = "";
        if (id != NO_ID) {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/editar_tarea_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", String.valueOf(id))
                    .appendQueryParameter("texto", etTexto.getText().toString())
                    .appendQueryParameter("tipo", iTipo + "")
                    .appendQueryParameter("estado", iEstado + "")
                    .build()
                    .toString();
        } else {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/insertar_tarea_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", null)
                    .appendQueryParameter("texto", etTexto.getText().toString())
                    .appendQueryParameter("tipo", iTipo + "")
                    .appendQueryParameter("estado", iEstado + "")
                    .build()
                    .toString();
        }

        Log.d("XYZ", "URL: " + URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Si procesa o no procesa
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), R.string.anuncio_creado_correctamente, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_EDITAR, 2);
        setResult(RESULT_OK, intent);
        finish();
    }

}