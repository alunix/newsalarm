package de.sidanner.newsalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by simon on 3/27/16.
 */
public class AlarmReceiver extends BroadcastReceiver implements MediaPlayer.OnPreparedListener, OnTaskCompleted {

    private MediaPlayer mPlayer;
    private Podcast podcast;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        AlarmSettings alarms = intent.getParcelableExtra("alarmSettings");


        podcast = alarms.toListen.get(0);
        podcast.getLastNews(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onTaskCompleted(String url) {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(R.raw.testsound);
            if (afd == null) return;
            try {
                mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();


            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        mPlayer.setOnPreparedListener(this);
        mPlayer.prepareAsync();
    }
}
