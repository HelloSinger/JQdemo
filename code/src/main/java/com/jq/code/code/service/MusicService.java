package com.jq.code.code.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.jq.code.code.business.DelayTimer;

public class MusicService extends CommonService {
    private MediaPlayer player;
    private DelayTimer checker = new DelayTimer(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            stopMusic();
            return true;
        }
    }));

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE));
        playMusic();
    }


    private void playMusic() {
        player.setLooping(true);
        player.start();
        checker.check(2 * 60 * 1000);
    }


    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }


    private void stopMusic() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player = null;
        }
    }
}
