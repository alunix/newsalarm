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


        String action = intent.getAction();
        if (action == "ringAlarm") {
            AlarmSettings alarms = intent.getParcelableExtra("alarmSettings");
            Intent i = new Intent(context, AlarmPlayerService.class);
            i.setAction(action);
            i.putExtra("alarmSettings", alarms);
            context.startService(i);
        } else if (action == "stopAlarm") {
            Intent i = new Intent(context, AlarmPlayerService.class);
            i.setAction(action);
            context.startService(i);
        }


    }
}
