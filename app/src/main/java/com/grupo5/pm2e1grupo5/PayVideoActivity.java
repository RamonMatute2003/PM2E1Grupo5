package com.grupo5.pm2e1grupo5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo5.pm2e1grupo5.config.Contactos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PayVideoActivity extends AppCompatActivity {
    VideoView videoView;
    Contactos element;
    private MediaExtractor mediaExtractor;
    private MediaCodec mediaCodec;
    private boolean isDecoding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_video);
        videoView = (VideoView) findViewById(R.id.videoViewRep);
        Button atras =findViewById(R.id.btn_atras);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent= new Intent(getApplicationContext(), ListContactsActivity.class);
                startActivity(new_intent);
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String video = prefs.getString("video", "");
        byte[] videBytes=video.getBytes();

        byte[] decodedVideoBytes = Base64.decode(videBytes, Base64.DEFAULT);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedVideoBytes);

        videoView.setVideoURI(Uri.parse(String.valueOf(decodedVideoBytes)));

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

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
        videoView.setVideoURI(videoUri);

        videoView.start();
    }
}