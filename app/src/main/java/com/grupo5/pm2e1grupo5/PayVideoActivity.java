package com.grupo5.pm2e1grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;
import com.grupo5.pm2e1grupo5.config.Contactos;

import java.io.ByteArrayInputStream;

public class PayVideoActivity extends AppCompatActivity {
    VideoView videoView;
    Contactos element;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_video);
        videoView = (VideoView) findViewById(R.id.videoViewRep);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String video = prefs.getString("video", "");

        Log.d("VIDEO PASADOOOOOOOO", "onCreate: "+video);


        byte[] decodedBytes = Base64.decode(video, Base64.DEFAULT);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
        Uri uri = Uri.parse(inputStream.toString());
        videoView.setVideoURI(uri);
        videoView.start();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();

    }

    public void decodeVideo(){

        byte[] decodedBytes = Base64.decode(element.getVideo(), Base64.DEFAULT);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
        Uri uri = Uri.parse(inputStream.toString());
        videoView.setVideoURI(uri);
        videoView.start();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.start();
    }
}