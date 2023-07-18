package com.grupo5.pm2e1grupo5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import androidx.annotation.NonNull;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    FrameLayout frameLayout;
    Button btnGrabarVideoReg, btnSalvarContactoReg,btnContactosSalvardosReg;
    EditText txtNombreReg,txtTelefonoReg,txtlongitudReg,txtLatitudReg;
    private RequestQueue requestQueue; //Cola de peticiones HTTP VOLLEY

    static final int REQUEST_VIDEO_CAPTURE = 1;
    String encodedVideo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView =(VideoView) findViewById(R.id.videoView);
        txtNombreReg = (EditText) findViewById(R.id.txtNombreReg);
        txtTelefonoReg = (EditText) findViewById(R.id.txtTelefonoReg);
        txtLatitudReg = (EditText) findViewById(R.id.txtLatitudReg);
        txtlongitudReg = (EditText) findViewById(R.id.txtLongitudReg);
        btnGrabarVideoReg = (Button) findViewById(R.id.btnGrabarVideoReg);
        btnSalvarContactoReg =(Button) findViewById(R.id.btnSalvarContactoReg);
        btnContactosSalvardosReg =(Button) findViewById(R.id.ContactosSalvadosReg);
        frameLayout = (FrameLayout) findViewById(R.id.frameVideo);

        txtLatitudReg.setEnabled(false);
        txtlongitudReg.setEnabled(false);

        btnGrabarVideoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });
        btnSalvarContactoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datoNombre = txtNombreReg.getText().toString();
                String datoTelefono = txtTelefonoReg.getText().toString();
                String datoLongitud = txtlongitudReg.getText().toString();
                String datoLatitud = txtLatitudReg.getText().toString();
                int count = 0;
                if (datoNombre.isEmpty()){
                    txtNombreReg.setError("Requrido: Favor ingrese un nombre");
                    count++;
                }
                if (datoTelefono.isEmpty()){
                    txtTelefonoReg.setError("Requrido: Favor ingrese un numero de Telefono");
                    count++;
                }
                if (datoLatitud.isEmpty()){
                    txtLatitudReg.setError("Requrido: Favor ingresar la Latitud ");
                    count++;
                }
                if (datoLongitud.isEmpty()){
                    txtlongitudReg.setError("Requrido: Favor ingresar la Longitud");
                    count++;
                }
                if (encodedVideo.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Favor Grabe un video",Toast.LENGTH_LONG).show();
                    count++;
                }

                if (count==0){
                    SaveContacts();
                }
            }
        });
        btnContactosSalvardosReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListContactsActivity.class);
                startActivity(intent);
            }
        });


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        obtenerUbicacion();
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

            videoView.setVideoURI(videoUri);
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(frameLayout);

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

            /*Bitmap videoFrame = retriever.getFrameAtTime();
            int originalWidth = videoFrame.getWidth();
            int originalHeight = videoFrame.getHeight();

            int compressedWidth = originalWidth / 2;
            int compressedHeight = originalHeight / 2;

            Bitmap compressedFrame = Bitmap.createScaledBitmap(videoFrame, compressedWidth, compressedHeight, false);
            compressedFrame.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Adjust the compression quality as needed
*/
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


















































    public void obtenerUbicacion(){
        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                txtLatitudReg.setText(""+location.getLatitude());
                txtlongitudReg.setText(""+location.getLongitude());
            }
            public void onStatusChanged(String provider, int status, Bundle extras){}

            public void onProviderEnable(String provider){}

            public void onProviderDisable(String provider){}
        };
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
    private void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_VIDEO_CAPTURE);
        }else{
//            CapturarVideo();
            dispatchTakeVideoIntent();
        }
    }
    public void CapturarVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    //Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//            Uri videoUri = intent.getData();
//            videoView.setVideoURI(videoUri);
//            MediaController mediaController = new MediaController(this);
//            videoView.setMediaController(mediaController);
//            mediaController.setAnchorView(frameLayout);
//        }
//    }

    private void SaveContacts() {
        requestQueue = Volley.newRequestQueue(this);
        Contactos contact = new Contactos();
        contact.setNombres(txtNombreReg.getText().toString());
        contact.setTelefono(txtTelefonoReg.getText().toString());
        contact.setLatitud(txtLatitudReg.getText().toString());
        contact.setLogintud(txtlongitudReg.getText().toString());
        //contact.setVideo(encodedVideo);
        JSONObject jsonContact = new JSONObject();
        try {
            jsonContact.put("nombre",contact.getNombres());
            jsonContact.put("telefono",contact.getTelefono());
            jsonContact.put("latitud",contact.getLatitud());
            jsonContact.put("longitud",contact.getLogintud());
            jsonContact.put("video",encodedVideo);
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RestApiMethods.apiPost, jsonContact, new Response.Listener<JSONObject>() {
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