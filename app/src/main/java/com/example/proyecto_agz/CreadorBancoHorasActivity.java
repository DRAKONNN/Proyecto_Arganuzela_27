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

public class CreadorBancoHorasActivity extends AppCompatActivity {

    private TextView tvTexto;
    private EditText etNombre;
    private TextView tvHorasNum;
    private Button btCrearBanco;

    private final static int NO_ID = -1;

    public int idEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creador_banco_horas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvTexto = findViewById(R.id.tvTexto);
        etNombre = findViewById(R.id.etNombre);
        tvHorasNum = findViewById(R.id.tvHorasNum);
        btCrearBanco = findViewById(R.id.btCrearBanco);

        Intent intent = getIntent();
        idEditar = Integer.parseInt(intent.getStringExtra(MainActivity.KEY_EDITAR));

        if (idEditar != NO_ID) {

            //Cambiar Views
            String nombre = intent.getStringExtra(MainActivity.NOMBRE_EDITAR);
            String horas = intent.getStringExtra(MainActivity.HORAS_EDITAR);
            Log.d("XYZ", "Intent: " + nombre + horas);
            etNombre.setText(nombre);
            tvHorasNum.setText(horas);
            tvTexto.setText(R.string.editar_militante);
            btCrearBanco.setText(R.string.editar_militante);
        }
    }

    public void onClickCrearBanco(View view) {

        int nombreLength = etNombre.getText().toString().trim().length();
        int horasLength = tvHorasNum.getText().toString().trim().length();

        if (nombreLength == 0 && horasLength == 0){

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
        if (id != NO_ID) {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/editar_banco_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", String.valueOf(id))
                    .appendQueryParameter("nombre", etNombre.getText().toString())
                    .appendQueryParameter("horas", tvHorasNum.getText().toString())
                    .build()
                    .toString();
        } else {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/insertar_banco_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", null)
                    .appendQueryParameter("nombre", etNombre.getText().toString())
                    .appendQueryParameter("horas", tvHorasNum.getText().toString())
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

    public void onClickRestar(View view) {

        float horas = Float.parseFloat(tvHorasNum.getText().toString());
        horas -= 0.5;
        tvHorasNum.setText(horas + "");
    }

    public void onClickSumar(View view) {

        float horas = Float.parseFloat(tvHorasNum.getText().toString());
        horas += 0.5;
        tvHorasNum.setText(horas + "");
    }
}