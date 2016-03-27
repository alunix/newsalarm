package de.sidanner.newsalarm;

import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class AlarmActivity extends ListActivity implements TimePickerDialog.OnTimeSetListener {

    ArrayList<Alarm> listItems = new ArrayList<Alarm>();
    ArrayAdapter<Alarm> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addAlarmFAB);

        adapter = new AlarmAdapter(this,
                android.R.layout.simple_list_item_1,
                listItems);

        setListAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView addAlarmText = (TextView) findViewById(R.id.addNewAlarmsTxt);
                addAlarmText.setVisibility(View.INVISIBLE);

                showTimePickerDialog(v);

            }
        });

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
        listItems.add(new Alarm(getApplicationContext(), false, hourOfDay, minute));
        adapter.notifyDataSetChanged();
    }
}
