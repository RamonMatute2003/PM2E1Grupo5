package com.grupo5.pm2e1grupo5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupo5.pm2e1grupo5.config.Contactos;
import com.grupo5.pm2e1grupo5.config.ListAdapter;
import com.grupo5.pm2e1grupo5.config.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListContactsActivity extends AppCompatActivity {
    Button btnAtras;
    private RequestQueue requestQueue; //Cola de peticiones HTTP VOLLEY
    List<Contactos> elements;
    int REQUEST_EDIT_CONTACT=0;
    EditText txtbuscar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);
        btnAtras = (Button) findViewById(R.id.bntAtrasList);
        txtbuscar = (EditText) findViewById(R.id.txtBuscarContact);

        txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        elements = new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);

        SelectedContacts();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_CONTACT && resultCode == RESULT_OK) {
            SelectedContacts();
        }
    }

    private void SelectedContacts() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RestApiMethods.apiGet, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONArray jsonArray=response;
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonContac = new JSONObject(jsonArray.getString(i));

                        String idcontacto = jsonContac.getString("idcontacto");
                        String nombre = jsonContac.getString("nombre");
                        String telefono = jsonContac.getString("telefono");
                        String longitud = jsonContac.getString("longitud");
                        String latitud = jsonContac.getString("latitud");
                        String video = jsonContac.getString("video");
                        elements.add(new Contactos(idcontacto,nombre,telefono,latitud,longitud,video));
                        Log.e("w",""+elements);
                    }
                    LlenarContactos();
                }catch(JSONException e){
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
        ListAdapter listContactsAdapter = new ListAdapter(elements, this, new ListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Contactos item) {
                dialogoOpciones(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.listContact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listContactsAdapter);
    }

    private void dialogoOpciones(Contactos item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] elementos = {"Ir al Mapa","Reproducir Video","Editar Contacto","Eliminar Contacto"};
        builder.setTitle("Acciones")
                .setItems(elementos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String elementoSeleccionado = elementos[which];

                        if (elementoSeleccionado == elementos[0]){ //Ver mapa
                            Intent intent = new Intent(getApplicationContext(), Maps_Activity.class);
                            intent.putExtra("la",item.getLatitud());
                            intent.putExtra("lo",item.getLogintud());
                            intent.putExtra("name",item.getNombres());
                            startActivity(intent);
                        }
                        if (elementoSeleccionado == elementos[1]){ //Reproducir Video
                            Intent videoIntent = new Intent(getApplicationContext(),PayVideoActivity.class);


                            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("video", item.getVideo());
                            editor.commit();



                            startActivity(videoIntent);
                        }
                        if (elementoSeleccionado == elementos[2]){ //Editar Contacto
                            Intent intent = new Intent(getApplicationContext(), EditContactActivity.class);
                            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("id", item.getId());
                            editor.putString("nombre", item.getNombres());
                            editor.putString("telefono", item.getTelefono());
                            editor.putString("latitud", item.getLatitud());
                            editor.putString("longitud", item.getLogintud());
                            editor.putString("video", item.getVideo());
                            editor.commit();
                            startActivity(intent);

                            finish();
                        }
                        if (elementoSeleccionado==elementos[3]){//Eliminar Contacto
                            DialogEliminarContacto(item);
                        }

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void DialogEliminarContacto(Contactos item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar contacto");
        builder.setMessage("¿Estás seguro de que deseas eliminar a " + item.getNombres() + " ?");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EliminarContacto(item);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void EliminarContacto(Contactos item){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, RestApiMethods.apiDel+"?idcontacto="+item.getId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String mensaje = null;
                try {
                    mensaje = response.getString("message");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
        finish();
        startActivity(getIntent());
    }

}