
package com.example.proyecto_agz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private EditText etPassword;

    public final static String KEY_USUARIO = "";

    private final static int ID_MIILIT = 1;
    private final static int ID_ADMIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getActionBar().hide();

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.usuarios, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        etPassword = findViewById(R.id.etPassword);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == ID_MIILIT || position == ID_ADMIN){

            etPassword.setVisibility(View.VISIBLE);
        } else {
            etPassword.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClickEntrar(View view) {

        Intent intent = new Intent(this, MainActivity.class);

        if (spinner.getSelectedItemPosition() == 0){

            intent.putExtra(KEY_USUARIO, spinner.getSelectedItemPosition());
            intent.putExtra(KEY_USUARIO, 0);
            startActivity(intent);
        }

        if (spinner.getSelectedItemPosition() != 0){

            if (etPassword.getText().toString().trim().length() == 0){

                Toast.makeText(this,R.string.sin_rellenar, Toast.LENGTH_SHORT).show();
            } else {

                buscarUsuario(spinner.getSelectedItemPosition());
            }
        }
    }

    public void buscarUsuario(int id) {

        HttpsTrustManager.allowAllSSL();

        String URL = Uri
                .parse("https://sombrous-livers.000webhostapp.com/select_usuario_agz.php")
                .buildUpon()
                .appendQueryParameter("id", String.valueOf(id))
                .build()
                .toString();

        Log.d("XYZ", URL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    //Se sacan los datos del JSON
                    String idJSON = response.getString("id");
                    String rangoJSON = response.getString("rango");
                    String passwordJSON = response.getString("password");

                    if (etPassword.getText().toString().equals(passwordJSON)){

                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.putExtra(KEY_USUARIO, id);
                        startActivity(intent2);
                    } else {

                        Toast.makeText(getApplicationContext(), R.string.contrasena_incorrecta, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("XYZ", error + "");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}