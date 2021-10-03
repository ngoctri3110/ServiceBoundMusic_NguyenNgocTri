package com.example.musicboundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private MusicService musicService;
    private boolean isBound = false;
//    private ServiceConnection connection;
    TextView tvStart,tvFinish;
    SeekBar sbTime;
    ImageView imgPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        final ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        final ImageView imgTua = (ImageView) findViewById(R.id.imgTua);
        final ImageView imgTuaVe = (ImageView) findViewById(R.id.imgTuaVe);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvFinish = (TextView) findViewById(R.id.tvFinish);
        sbTime = (SeekBar) findViewById(R.id.sbTime);

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.getMyPlayer().getMediaPlayer().seekTo(sbTime.getProgress());
            }
        });


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
                    Time();
                    TimeOut();
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
        imgTuaVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound){
                    if(musicService.isPlaying()){
                        musicService.backForward();
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
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder binder = (MusicService.MyBinder) service;
            musicService = binder.getService();
            isBound = true;
            Time();
            TimeOut();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };
    private void TimeOut() {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        tvFinish.setText(format.format(musicService.getMyPlayer().getMediaPlayer().getDuration()));
        sbTime.setMax(musicService.getMyPlayer().getMediaPlayer().getDuration());
    }

    private void Time() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                tvStart.setText(format.format(musicService.getMyPlayer().getMediaPlayer().getCurrentPosition()));
                sbTime.setProgress(musicService.getMyPlayer().getMediaPlayer().getCurrentPosition());
                musicService.getMyPlayer().getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Time();
                        TimeOut();
                        imgPlay.setImageResource(R.drawable.play);
                    }
                });
                handler.postDelayed(this, 1000);
            }
        }, 100);
    }

}