package com.grupo5.pm2e1grupo5;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String video = prefs.getString("video", "");

        Log.d("VIDEO PASADOOOOOOOO", "onCreate: " + video);


        byte[] decodedBytes = Base64.decode(video, Base64.DEFAULT);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

        File videoFile = null;
        Log.e("w",""+videoFile);
        try {
            /*Log.e("w",""+videoFile);
            videoFile = File.createTempFile("temp_video", ".mp4", getCacheDir());
            Log.e("w",""+videoFile);*/

            videoFile = new File("data/data/com.grupo5.pm2e1grupo5/cache/volley/", "temp_video.mp4");
            FileOutputStream outputStream = new FileOutputStream(videoFile);
            outputStream.write(decodedBytes);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Error",""+e);
        }

        String videoUrl = null;
        if (videoFile != null) {
            videoUrl = videoFile.getAbsolutePath();
        }

        //Uri uri = Uri.parse("data/data/com.grupo5.pm2e1grupo5/cache/volley/"+(videoUrl.substring(42, videoUrl.length())));
        Uri uri = Uri.parse("data/data/com.grupo5.pm2e1grupo5/cache/volley/2023-05-16.mp4");
        Log.e("url", videoUrl);
        videoView.setVideoURI(uri);
    }
}