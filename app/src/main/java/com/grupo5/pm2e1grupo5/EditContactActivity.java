package com.grupo5.pm2e1grupo5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditContactActivity extends AppCompatActivity {
    VideoView videoViewEdit;
    FrameLayout frameLayoutEdit;
    Button btnGrabarVideoEdit, btnEditarContactoEdit,btnCancelEditContact;
    EditText txtNombreEdit,txtTelefonoEdit,txtlongitudEdit,txtLatitudEdit;
    String id;
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

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String video = prefs.getString("video", "");
        id = prefs.getString("id", "");

        byte[] videBytes=video.getBytes();

        byte[] decodedVideoBytes = Base64.decode(videBytes, Base64.DEFAULT);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedVideoBytes);

        videoViewEdit.setVideoURI(Uri.parse(String.valueOf(decodedVideoBytes)));

        MediaController mediaController = new MediaController(this);
        videoViewEdit.setMediaController(mediaController);
        mediaController.setAnchorView(videoViewEdit);

        File videoFile = new File(getFilesDir(), "video.mp4");
        try {
            FileOutputStream outputStream = new FileOutputStream(videoFile);
            outputStream.write(decodedVideoBytes);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Obtener la URI del archivo de video
        Uri videoUri = Uri.fromFile(videoFile);

        // Configurar el VideoView o MediaPlayer con la URI del archivo de video
        videoViewEdit.setVideoURI(videoUri);

        videoViewEdit.start();

        txtNombreEdit.setText(prefs.getString("nombre", ""));
        txtTelefonoEdit.setText(prefs.getString("telefono", ""));
        txtLatitudEdit.setText(prefs.getString("latitud", ""));
        txtlongitudEdit.setText(prefs.getString("longitud", ""));


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
//                    Intent resultIntent = new Intent();
//                    setResult(RESULT_OK, resultIntent);
                    Intent intent = new Intent(getApplicationContext(), ListContactsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btnCancelEditContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},REQUEST_VIDEO_CAPTURE);
        }else{
            //CapturarVideo();
            dispatchTakeVideoIntent();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();

            videoViewEdit.setVideoURI(videoUri);
            MediaController mediaController = new MediaController(this);
            videoViewEdit.setMediaController(mediaController);
            mediaController.setAnchorView(frameLayoutEdit);

            int targetBitRate = 2 * 1024 * 1024;
            int targetFrameRate = 30;

            InputStream inputStream = null;

            try {
                inputStream = getContentResolver().openInputStream(videoUri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, videoUri);

            byte[] buffer = new byte[1024];
            int len;
            try {
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
            }catch (Exception e){

            }

            byte[] videoBytes=byteArrayOutputStream.toByteArray();
            encodedVideo = Base64.encodeToString(videoBytes, Base64.DEFAULT);
        }
    }
    String encodedVideo="";
    private void SaveEditContact() {
        requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonContact = new JSONObject();
        try {
            jsonContact.put("nombre",txtNombreEdit.getText().toString());
            jsonContact.put("telefono",txtTelefonoEdit.getText().toString());
            jsonContact.put("latitud",txtLatitudEdit.getText().toString());
            jsonContact.put("longitud",txtlongitudEdit.getText().toString());
            jsonContact.put("video",encodedVideo);
            jsonContact.put("idcontacto",id);
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