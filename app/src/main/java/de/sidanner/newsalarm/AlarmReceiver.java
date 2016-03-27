package de.sidanner.newsalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by simon on 3/27/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmSettings alarms = intent.getParcelableExtra("alarmSettings");

        Intent i = new Intent(context, AlarmPlayerService.class);
        i.setAction("de.sidanner.newsalarm.playalarm");
        i.putExtra("alarmSettings", alarms);
        context.startService(i);
    }
}
