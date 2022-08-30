package com.example.katsino_bd.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.katsino_bd.R;

public class BackgroundSoundService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.jazz);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("PLAY_AND_SEEK")) {
            if(mediaPlayer.isPlaying() == false) {
                mediaPlayer.start();
                int position = App.instance().settings.getInt("position",0);
                mediaPlayer.seekTo(position);
                //Toast.makeText(getApplicationContext(), "Music Is Playing from last position", Toast.LENGTH_SHORT).show();
                App.instance().editor.putBoolean("lastState_playingMusic",true);
                App.instance().editor.apply();
                App.instance().editor.commit();
            }
        }
        if (intent.getAction().equals("PLAY")) {
            if(mediaPlayer.isPlaying() == false) {
                mediaPlayer.start();
                //Toast.makeText(getApplicationContext(), "Music Is Playing", Toast.LENGTH_SHORT).show();
                App.instance().editor.putBoolean("lastState_playingMusic",true);
                App.instance().editor.apply();
                App.instance().editor.commit();
            }
        }
        if (intent.getAction().equals("STOP")) {
            this.stopService(intent);
            //Toast.makeText(getApplicationContext(), "Music Stopped",    Toast.LENGTH_SHORT).show();
            App.instance().editor.putBoolean("lastState_playingMusic",false);
            App.instance().editor.apply();
            App.instance().editor.commit();

        }
        if (intent.getAction().equals("APP_NOT_VISIBLE")) {
            if(mediaPlayer.isPlaying()) {
                int position = mediaPlayer.getCurrentPosition();
                this.stopService(intent);
                //Toast.makeText(getApplicationContext(), "Music stopped because app is not visible anymore", Toast.LENGTH_SHORT).show();
                App.instance().editor.putBoolean("lastState_playingMusic",true);//we set it to true so we can continue playing when the app become visible again
                App.instance().editor.putInt("position",position);
                App.instance().editor.apply();
                App.instance().editor.commit();
            }
        }
        return startId;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }
}
