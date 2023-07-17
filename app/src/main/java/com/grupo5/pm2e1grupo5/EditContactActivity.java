package com.grupo5.pm2e1grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.grupo5.pm2e1grupo5.config.Contactos;
import com.grupo5.pm2e1grupo5.config.RestApiMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class EditContactActivity extends AppCompatActivity {
    VideoView videoViewEdit;
    FrameLayout frameLayoutEdit;
    Button btnGrabarVideoEdit, btnEditarContactoEdit,btnCancelEditContact;
    EditText txtNombreEdit,txtTelefonoEdit,txtlongitudEdit,txtLatitudEdit;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    Contactos element;
    private RequestQueue requestQueue; //Cola de peticiones HTTP VOLLEY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        videoViewEdit =(VideoView) findViewById(R.id.videoViewEdit);
        txtNombreEdit = (EditText) findViewById(R.id.txtNombreEdit);
        txtTelefonoEdit = (EditText) findViewById(R.id.txtTelefonoEdit);
        txtLatitudEdit = (EditText) findViewById(R.id.txtLatitudEdit);
        txtlongitudEdit = (EditText) findViewById(R.id.txtLongitudEdit);
        btnGrabarVideoEdit = (Button) findViewById(R.id.btnGrabarVideoEdit);
        btnEditarContactoEdit =(Button) findViewById(R.id.btnEditarContactoEdit);
        btnCancelEditContact =(Button) findViewById(R.id.btnCancelarEdit);
        frameLayoutEdit = (FrameLayout) findViewById(R.id.frameVideoEdit);


        element = (Contactos) getIntent().getSerializableExtra("contact");
        txtNombreEdit.setText(element.getNombres());
        txtTelefonoEdit.setText(element.getTelefono());
        txtLatitudEdit.setText(element.getLatitud());
        txtlongitudEdit.setText(element.getLogintud());

        btnGrabarVideoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        btnEditarContactoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datoNombre = txtNombreEdit.getText().toString();
                String datoTelefono = txtTelefonoEdit.getText().toString();
                String datoLongitud = txtlongitudEdit.getText().toString();
                String datoLatitud = txtLatitudEdit.getText().toString();
                int count = 0;
                if (datoNombre.isEmpty()){
                    txtNombreEdit.setError("Requrido: Favor ingrese un nombre");
                    count++;
                }
                if (datoTelefono.isEmpty()){
                    txtTelefonoEdit.setError("Requrido: Favor ingrese un numero de Telefono");
                    count++;
                }
                if (datoLatitud.isEmpty()){
                    txtLatitudEdit.setError("Requrido: Favor ingresar la Latitud ");
                    count++;
                }
                if (datoLongitud.isEmpty()){
                    txtlongitudEdit.setError("Requrido: Favor ingresar la Longitud");
                    count++;
                }
//                if (videoView.getCurrentPosition()==0){
//                    Toast.makeText(getApplicationContext(),"Favor Grabe un video",Toast.LENGTH_LONG).show();
//                    count++;
//                }
                if (count==0){
                    SaveEditContact();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
        btnCancelEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},REQUEST_VIDEO_CAPTURE);
        }else{
            CapturarVideo();
        }
    }
    public void CapturarVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            videoViewEdit.setVideoURI(videoUri);
            MediaController mediaController = new MediaController(this);
            videoViewEdit.setMediaController(mediaController);
            mediaController.setAnchorView(videoViewEdit);
        }
    }

    private void SaveEditContact() {
        requestQueue = Volley.newRequestQueue(this);
        Contactos contact = new Contactos();
        contact.setNombres(txtNombreEdit.getText().toString());
        contact.setTelefono(txtTelefonoEdit.getText().toString());
        contact.setLatitud(txtLatitudEdit.getText().toString());
        contact.setLogintud(txtlongitudEdit.getText().toString());
        contact.setVideo("Video");
        contact.setId(element.getId());
        JSONObject jsonContact = new JSONObject();
        try {
            jsonContact.put("nombre",contact.getNombres());
            jsonContact.put("telefono",contact.getTelefono());
            jsonContact.put("latitud",contact.getLatitud());
            jsonContact.put("longitud",contact.getLogintud());
            jsonContact.put("video","video");
            jsonContact.put("idcontacto",contact.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RestApiMethods.apiUpd, jsonContact, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String mensaje = response.getString("message");
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("task","OnErrorResponse----> " + error.toString());
            }
        }
        );

        requestQueue.add(request);

    }

}