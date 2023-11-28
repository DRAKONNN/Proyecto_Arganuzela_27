package com.example.proyecto_agz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class CreadorSalaActivity extends AppCompatActivity {

    private TextView tvTexto;
    private EditText etNombreSala;
    private Button btElegirFechaInicio;
    private Button btElegirHoraInicio;
    private Button btElegirFechaFin;
    private Button btElegirHoraFin;
    private Button btCrearSala;

    private int botonPresionado;
    private final static int ID_FECHA_INICIO = 0;

    private final static int NO_ID = -1;

    public int idEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creador_sala);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvTexto = findViewById(R.id.tvTexto);
        etNombreSala = findViewById(R.id.etNombreSala);
        btElegirFechaInicio = findViewById(R.id.btElegirFechaInicio);
        btElegirHoraInicio = findViewById(R.id.btElegirHoraInicio);
        btElegirFechaFin = findViewById(R.id.btElegirFechaFin);
        btElegirHoraFin = findViewById(R.id.btElegirHoraFin);
        btCrearSala = findViewById(R.id.btCrearSala);


        Intent intent = getIntent();
        idEditar = Integer.parseInt(intent.getStringExtra(MainActivity.KEY_EDITAR));

        if (idEditar != NO_ID) {

            //Cambiar Views
            String nombre = intent.getStringExtra(MainActivity.NOMBRE_EDITAR);
            String fechaInicio = intent.getStringExtra(MainActivity.FECHA_INICIO_EDITAR);
            String[] fechaInicioDiv = fechaInicio.split(" ");
            String fechaFin = intent.getStringExtra(MainActivity.FECHA_FIN_EDITAR);
            String[] fechaFinDiv = fechaFin.split(" ");

            etNombreSala.setText(nombre);
            btElegirFechaInicio.setText(fechaInicioDiv[0]);
            btElegirFechaFin.setText(fechaFinDiv[0]);
            btElegirHoraInicio.setText(fechaInicioDiv[1]);
            btElegirHoraFin.setText(fechaFinDiv[1]);

            tvTexto.setText(R.string.editar_sala);
            btCrearSala.setText(R.string.editar_sala);
        }
    }

    public void onClickCrearSala(View view) {

        int nombreLength = etNombreSala.getText().toString().trim().length();


        if (nombreLength == 0 || faltanHorarios()){

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
        String fecha_inicio = btElegirFechaInicio.getText().toString() + " " + btElegirHoraInicio.getText().toString();
        String fecha_fin = btElegirFechaFin.getText().toString() + " " + btElegirHoraFin.getText().toString();
        if (id != NO_ID) {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/editar_sala_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", String.valueOf(id))
                    .appendQueryParameter("nombre", etNombreSala.getText().toString())
                    .appendQueryParameter("fecha_inicio", fecha_inicio)
                    .appendQueryParameter("fecha_fin", fecha_fin)
                    .build()
                    .toString();
        } else {

            URL = Uri
                    .parse("https://sombrous-livers.000webhostapp.com/insertar_sala_agz.php")
                    .buildUpon()
                    .appendQueryParameter("id", null)
                    .appendQueryParameter("nombre", etNombreSala.getText().toString())
                    .appendQueryParameter("fecha_inicio", fecha_inicio)
                    .appendQueryParameter("fecha_fin", fecha_fin)
                    .build()
                    .toString();
        }

        Log.d("XYZ", "URL: " + URL);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Si procesa o no procesa
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), R.string.sala_reservada_correctamente, Toast.LENGTH_SHORT).show();
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

    public void onClickElegirFecha(View view) {

        final Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int anio = c.get(Calendar.YEAR);

        Button button = (Button) view;
        if (button == btElegirFechaInicio){

            botonPresionado = ID_FECHA_INICIO;
            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        btElegirFechaInicio.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, dia, mes, anio);
                datePickerDialog.show();
            }*/
            showDatePickerDialog(btElegirFechaInicio);
        } else {

            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        btElegirFechaFin.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, dia, mes, anio);
                datePickerDialog.show();
            }*/
            showDatePickerDialog(btElegirFechaFin);
        }
    }

    public void onClickElegirHora(View view) {

        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        Button button = (Button) view;
        if (button == btElegirHoraInicio){

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String strHour = digitoCompleto(hourOfDay + "");
                    String strMin = digitoCompleto(minute + "");

                    btElegirHoraInicio.setText(strHour + ":" + strMin);
                }
            }, hora, min, true);
            timePickerDialog.show();
        } else {

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String strHour = digitoCompleto(hourOfDay + "");
                    String strMin = digitoCompleto(minute + "");

                    btElegirHoraFin.setText(strHour + ":" + strMin);
                }
            }, hora, min, true);
            timePickerDialog.show();
        }

    }

    //Para añadir 0 si hace falta
    public String digitoCompleto(String str) {

        if (str.length() == 1) {

            str = 0 + "" + str;
        }
        return str;
    }

    public boolean faltanHorarios() {

        if (btElegirFechaInicio.getText().toString().equals(R.string.elegir_fecha_inicio)){

            return true;
        }

        if (btElegirFechaFin.getText().toString().equals(R.string.elegir_fecha_fin)){

            return true;
        }

        if (btElegirHoraInicio.getText().toString().equals(R.string.elegir_hora_inicio)){

            return true;
        }

        if (btElegirHoraFin.getText().toString().equals(R.string.elegir_hora_fin)){

            return true;
        }

        return false;
    }

    private void showDatePickerDialog(Button button) {

        CreadorSalaActivity.DatePickerFragment newFragment = CreadorSalaActivity.DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //+1 porque enero es 0
                int finalMonth = (month + 1);

                String strDay = digitoCompleto(day + "");
                String strMonth = digitoCompleto(finalMonth + "");

                String selectedDate = strDay  + "/" + strMonth + "/" + year;
                button.setText(selectedDate);

            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }

    }

}