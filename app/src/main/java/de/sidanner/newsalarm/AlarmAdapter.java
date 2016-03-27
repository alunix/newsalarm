package de.sidanner.newsalarm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by simon on 3/27/16.
 */
public class AlarmAdapter extends ArrayAdapter<Alarm> {

    private Activity activity;
    private ArrayList<Alarm> alarmList;
    private LayoutInflater inflater;


    public AlarmAdapter(Activity activity, int textViewResourceId, ArrayList<Alarm> alarmList) {
        super(activity, textViewResourceId, alarmList);
        try {
            this.activity = activity;
            this.alarmList = alarmList;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.alarmlayout, null);
                holder = new ViewHolder();

                holder.display_date = (TextView) vi.findViewById(R.id.display_date);
                holder.display_repeating_string = (TextView) vi.findViewById(R.id.display_repeating);
                holder.display_delete = (Button) vi.findViewById(R.id.deleteAlarmButton);
                holder.display_repeating_check = (CheckBox) vi.findViewById(R.id.display_repeatingCheckbox);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            final Alarm alarmToDisplay = alarmList.get(position);
            String timeToDisplay = "" + alarmToDisplay.timeToRing.get(Calendar.HOUR_OF_DAY);
            timeToDisplay = timeToDisplay + ":";

            int minute = alarmToDisplay.timeToRing.get(Calendar.MINUTE);

            if (minute < 10) {
                timeToDisplay += "0" + minute;
            } else {
                timeToDisplay += minute;
            }


            holder.display_date.setText(timeToDisplay);
            holder.display_repeating_string.setText(alarmToDisplay.isRepeating ? "repeating" : "non-repeating");
            holder.display_repeating_check.setChecked(alarmToDisplay.isRepeating);

            holder.display_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Alarm alarm = alarmList.get(position);
                    alarm.cancel();
                    remove(alarm);
                    notifyDataSetChanged();
                }
            });


        } catch (Exception e) {


        }
        return vi;
    }

    @Override
    public void remove(Alarm alarm) {
        alarmList.remove(alarm);

        for (int i = 0; i < alarmList.size(); i++) {
            Alarm toRefresh = alarmList.get(i);
            toRefresh.setActive();
        }

    }

    public static class ViewHolder {
        public TextView display_date;
        public TextView display_repeating_string;
        public Button display_delete;
        public CheckBox display_repeating_check;

    }
}
