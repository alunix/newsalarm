package de.sidanner.newsalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simon on 3/27/16.
 */
public class Alarm {
    public boolean isRepeating;

    public Calendar timeToRing;

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;


    public Alarm(Context context, boolean repeating, int hour, int minute) {
        isRepeating = true;

        timeToRing = Calendar.getInstance();

        timeToRing.set(Calendar.HOUR_OF_DAY, hour);
        timeToRing.set(Calendar.MINUTE, minute);
        timeToRing.set(Calendar.SECOND, 0);

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        ArrayList<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast());

        AlarmSettings alarmExecution = new AlarmSettings(podcasts);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("de.sidanner.newsalarm.alarms");
        intent.putExtra("alarmSettings", alarmExecution);

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);


        if (repeating) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeToRing.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, timeToRing.getTimeInMillis(), pendingIntent);
        }
    }

    public void cancel() {
        alarmMgr.cancel(pendingIntent);
        pendingIntent.cancel();
    }


}


