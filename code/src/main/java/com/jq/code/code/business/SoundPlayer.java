package com.jq.code.code.business;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/13.
 */

public class SoundPlayer {
    private MediaPlayer mp;
    private AudioManager am;

    public SoundPlayer(Context context, String filename) {
        try {
            mp = new MediaPlayer();
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            AssetFileDescriptor fd = context.getAssets().openFd(filename);
            mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (mp.isPlaying()) {
//            mp.stop();
//            mp.reset();
        } else {
            mp.start();
        }
    }

    public void release() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    public MediaPlayer getMp() {
        return mp;
    }
}
