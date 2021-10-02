package com.example.musicboundservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MyPlayer myPlayer;
    private IBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        myPlayer = new MyPlayer(this);
        binder = new MyBinder();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        myPlayer.play();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        myPlayer.stop();
        return super.onUnbind(intent);
    }
    public void fastForward(){
        myPlayer.fastForward(16000); // seek to ...
    }

    public boolean isPlaying(){
        return myPlayer.isPlaying();
    }
    public void pause(){
        myPlayer.pause();
    }
    public void play(){
        myPlayer.play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myPlayer.release();
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
    class MyPlayer {

        private MediaPlayer mediaPlayer;

        public MyPlayer(Context context) {
            // add song to ...
            mediaPlayer = MediaPlayer.create(
                    context, R.raw.shapeofyou);
            // set mode loop
            mediaPlayer.setLooping(true);
        }
        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }
        public void release(){
            mediaPlayer.release();
        }
        public void fastForward(int pos){
            mediaPlayer.seekTo(pos);
        }

        public void play() {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
        public void pause(){
            mediaPlayer.pause();
        }

        public void stop() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        }
}
