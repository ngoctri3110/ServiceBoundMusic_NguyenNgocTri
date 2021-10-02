package com.example.musicboundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MusicService musicService;
    private boolean isBound = false;
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imgPlay = (ImageView) findViewById(R.id.imgPlay);
        final ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        final ImageView imgTua = (ImageView) findViewById(R.id.imgTua);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MyBinder binder = (MusicService.MyBinder) service;
                musicService = binder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };
        final Intent intent = new Intent(MainActivity.this, MusicService.class);

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound) {
                    if (musicService.isPlaying()) {
                        musicService.pause();
                        imgPlay.setImageResource(R.drawable.play);
                    } else {
                        musicService.play();
                        imgPlay.setImageResource(R.drawable.ic_pause);
                    }
                }else{
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                    imgPlay.setImageResource(R.drawable.ic_pause);
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService.isPlaying()){
                    unbindService(connection);
                    isBound = false;
                    Toast.makeText(MainActivity.this,
                            "Service ngưng hoạt động", Toast.LENGTH_SHORT).show();
                    imgPlay.setImageResource(R.drawable.play);

                }else{
                    unbindService(connection);
                    isBound = false;
                    Toast.makeText(MainActivity.this,
                            "Service ngưng hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgTua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound){
                    if(musicService.isPlaying()){
                        musicService.fastForward();
                    }else{
                        Toast.makeText(MainActivity.this,
                                "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}