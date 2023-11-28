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

public class CreadorAnuncioActivity extends AppCompatActivity {

    private TextView tvTexto ;
    private EditText etTitulo;
    private EditText etDescripcion;
    private Button btCrearAnuncio;

    private final static int NO_ID = -1;

    public int idEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creador_anuncio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvTexto = findViewById(R.id.tvTexto);
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        btCrearAnuncio = findViewById(R.id.btCrearAnuncio);

        Intent intent = getIntent();
        idEditar = Integer.parseInt(intent.getStringExtra(MainActivity.KEY_EDITAR));

        if (idEditar != NO_ID) {

            //Cambiar Views
            String titulo = intent.getStringExtra(MainActivity.TITULO_EDITAR);
            String descripcion = intent.getStringExtra(MainActivity.DESCRIPCION_EDITAR);
            etTitulo.setText(titulo);
            etDescripcion.setText(descripcion);
            tvTexto.setText(R.string.editar_anuncio);
            btCrearAnuncio.setText(R.string.editar_anuncio);
        }
    }

    public void onClickCrearAnuncio(View view) {

        int tituloLength = etTitulo.getText().toString().trim().length();
        int descripcionLength = etDescripcion.getText().toString().trim().length();

        if (tituloLength == 0 && descripcionLength == 0){

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

        String URL = "";
        if (id != -1) {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/editar_anuncio_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", String.valueOf(id))
                    .appendQueryParameter("titulo", etTitulo.getText().toString())
                    .appendQueryParameter("descripcion", etDescripcion.getText().toString())
                    .appendQueryParameter("imagen", null)
                    .appendQueryParameter("fecha", calcularGMT())
                    .build()
                    .toString();
        } else {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/insertar_anuncio_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", null)
                    .appendQueryParameter("titulo", etTitulo.getText().toString())
                    .appendQueryParameter("descripcion", etDescripcion.getText().toString())
                    .appendQueryParameter("imagen", null)
                    .appendQueryParameter("fecha", calcularGMT())
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

    public String calcularGMT() {

        //Se saca el dia actual y se le resta 1 dia
        Calendar calendar = Calendar.getInstance();
        Date date1 = calendar.getTime();

        //Se transforman las fechas al formato necesario
        SimpleDateFormat formatoTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = formatoTimestamp.format(date1);
        
        //Se pasan las fechas a la zona horaria de GMT (Hora 0)
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        formatoTimestamp.setTimeZone(timeZone);
        time1 = formatoTimestamp.format(date1);

        return time1;
    }
}