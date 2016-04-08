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

    public boolean isEnabled;

    public Calendar timeToRing;

    private AlarmManager alarmMgr;

    private Context context;


    public Alarm(Context context, boolean repeating, int hour, int minute) {
        this.context = context;

        isRepeating = repeating;

        isEnabled = true;

        timeToRing = Calendar.getInstance();

        timeToRing.set(Calendar.HOUR_OF_DAY, hour);
        timeToRing.set(Calendar.MINUTE, minute);
        timeToRing.set(Calendar.SECOND, 0);

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        setActive();


    }

    public Alarm(Context context, Calendar time, boolean repeating, boolean isEnabled) {
        this.context = context;
        timeToRing = time;
        isRepeating = repeating;
        this.isEnabled = isEnabled;

        if (timeToRing.before(Calendar.getInstance())) {
            timeToRing.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        setActive();
    }

    public void cancel() {
        PendingIntent intent = makePendingIntent(getIntent());
        alarmMgr.cancel(intent);
    }

    private Intent getIntent() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("de.sidanner.newsalarm.alarms");
        return intent;
    }


    private PendingIntent makePendingIntent(Intent intent) {
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    public void setActive() {
        if (!isEnabled) {
            return;
        }
        ArrayList<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast());

        AlarmSettings alarmExecution = new AlarmSettings(podcasts);

        Intent intent = getIntent();
        intent.setAction("ringAlarm");
        intent.putExtra("alarmSettings", alarmExecution);

        PendingIntent pendingIntent = makePendingIntent(intent);


        sendToAlarmManager(pendingIntent);
    }

    public void sendToAlarmManager(PendingIntent pendingIntent) {
        if (isRepeating) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeToRing.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, timeToRing.getTimeInMillis(), pendingIntent);
        }
    }
}


