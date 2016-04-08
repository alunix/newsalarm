package de.sidanner.newsalarm;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by simon on 3/27/16.
 */
public class AlarmPlayerService extends Service implements OnTaskCompleted, MediaPlayer.OnPreparedListener {

    private MediaPlayer mPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleCommand(intent);

        return START_NOT_STICKY;
    }

    private void handleCommand(Intent intent) {

        String action = intent.getAction();

        if (action == "stopAlarm") {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
        } else {
            AlarmSettings alarms = intent.getParcelableExtra("alarmSettings");
            Podcast podcast = alarms.toListen.get(0);
            podcast.getLastNews(this);
        }
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
            AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.testsound);
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
