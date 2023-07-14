package com.grupo5.pm2e1grupo5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    Button btnGrabarVideoReg, btnSalvarContactoReg,btnContactosSalvardosReg;
    EditText txtNombreReg,txtTelefonoReg,txtlongitudReg,txtLatitudReg;

    static final int REQUEST_VIDEO_CAPTURE = 1;


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
                if (datoNombre.isEmpty()){
                    txtNombreReg.setError("Requrido: Favor ingrese un nombre");
                }
                if (datoTelefono.isEmpty()){
                    txtTelefonoReg.setError("Requrido: Favor ingrese un numero de Telefono");
                }
                if (datoLatitud.isEmpty()){
                    txtLatitudReg.setError("Requrido: Favor incluir la Latitud ");
                }
                if (datoLongitud.isEmpty()){
                    txtlongitudReg.setError("Requrido: Debe de la Longitud");
                }
            }
        });
        btnContactosSalvardosReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            videoView.setVideoURI(videoUri);
        }
    }
}