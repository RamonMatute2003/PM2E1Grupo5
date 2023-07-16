package com.grupo5.pm2e1grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grupo5.pm2e1grupo5.config.Contactos;
import com.grupo5.pm2e1grupo5.config.ListAdapter;
import com.grupo5.pm2e1grupo5.config.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class ListContactsActivity extends AppCompatActivity {
    Button btnAtras;
    private RequestQueue requestQueue; //Cola de peticiones HTTP VOLLEY
    String[] dataListContact;
    List<Contactos> elements;
    String[][] arregloContactos;


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
        elements = new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);

        SelectedContacts();
//        LlenarContactos();
    }
    private void SelectedContacts() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RestApiMethods.apiGet, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray jsonArray = response;
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonContac = new JSONObject(jsonArray.getString(i));

                        String idcontacto = jsonContac.getString("idcontacto");
                        String nombre = jsonContac.getString("nombre");
                        String telefono = jsonContac.getString("telefono");
                        String longitud = jsonContac.getString("longitud");
                        String latitud = jsonContac.getString("latitud");
                        String video = jsonContac.getString("video");
                        elements.add(new Contactos(nombre,telefono));
                    }
                    LlenarContactos();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "onErrorResponse: " + error.toString() );
            }
        });
        requestQueue.add(request);
    }

    public void LlenarContactos(){
        ListAdapter listContactsAdapter = new ListAdapter(elements,this);
        RecyclerView recyclerView = findViewById(R.id.listContact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listContactsAdapter);
    }


}