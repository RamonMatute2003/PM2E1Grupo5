package com.grupo5.pm2e1grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grupo5.pm2e1grupo5.config.Contactos;
import com.grupo5.pm2e1grupo5.config.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListContactsActivity extends AppCompatActivity {
    Button btnAtras;
    private RequestQueue requestQueue; //Cola de peticiones HTTP VOLLEY
    String[] dataListContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);
        btnAtras = (Button) findViewById(R.id.bntAtrasList);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SelectedContacts();
    }
    private void SelectedContacts() {

        StringRequest request = new StringRequest(Request.Method.GET, RestApiMethods.apiGet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    dataListContact =new String[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonContac=jsonArray.getJSONObject(i);
                        String idcontacto = jsonContac.getString("idcontacto");
                        String nombre = jsonContac.getString("nombre");
                        String telefono = jsonContac.getString("telefono");
                        String longitud = jsonContac.getString("longitud");
                        String latitud = jsonContac.getString("latitud");
                        String video = jsonContac.getString("video");

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}