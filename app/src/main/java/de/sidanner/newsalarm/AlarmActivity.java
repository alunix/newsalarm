package de.sidanner.newsalarm;

import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AlarmActivity extends ListActivity implements TimePickerDialog.OnTimeSetListener {

    final String filename = "alarms.txt";
    ArrayList<Alarm> listItems = new ArrayList<Alarm>();
    ArrayAdapter<Alarm> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        adapter = new AlarmAdapter(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);

        FloatingActionButton addAlarmButton = (FloatingActionButton) findViewById(R.id.addAlarmFAB);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);

            }
        });

        FloatingActionButton stopRunningAlarmButton = (FloatingActionButton) findViewById(R.id.stopAlarmFAB);
        stopRunningAlarmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopPendingAlarm(v);
            }
        });

    }

    private void stopPendingAlarm(View v) {
        Intent intent = new Intent(v.getContext(), AlarmReceiver.class);
        intent.setAction("stopAlarm");
        sendBroadcast(intent);
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.listener = this;
        newFragment.show(getFragmentManager(), "timePicker");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listItems.add(new Alarm(getApplicationContext(), true, hourOfDay, minute));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

        List<String> alarms = new LinkedList<String>();

        try {
            InputStream inputStream = openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                alarms.add(line);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        for (int i = 0; i < alarms.size(); i++) {
            String alarm = alarms.get(i);

            String alarmValues[] = alarm.split("\\|");

            boolean repeating = Boolean.parseBoolean(alarmValues[0]);
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(Long.parseLong(alarmValues[1]));

            boolean isEnabled = Boolean.parseBoolean(alarmValues[2]);

            Alarm al = new Alarm(this, time, repeating, isEnabled);
            listItems.add(al);
        }
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onStop() {
        super.onStop();

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);

            StringBuilder save = new StringBuilder();
            for (int i = 0; i < listItems.size(); i++) {

                Alarm toWrite = listItems.get(i);
                save.append(toWrite.isRepeating);
                save.append("|");
                save.append(toWrite.timeToRing.getTimeInMillis());
                save.append("|");
                save.append(toWrite.isEnabled);
                save.append("\n");
            }
            Log.d("bla", "writing to file: " + save.toString());
            outputStream.write(save.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
