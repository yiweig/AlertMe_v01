package com.yiweigao.alertme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AlertMe extends ActionBarActivity {

    private byte alarmCountdownValue;    // max value 127
    private static final short SETTINGS_INFO = 1;   // used for startActivityForResult()

    private TextView alarmCountdownText;
    private Button startAlarmButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_me);

        alarmCountdownText = (TextView) findViewById(R.id.alarm_countdown_text);
        alarmCountdownText.setText("Not initialized yet");

        startAlarmButton = (Button) findViewById(R.id.start_alarm_button);
        updateAlarmCountdown();

        final Alarm newAlarm = new Alarm(this, alarmCountdownValue);

        startAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAlarm.startCountdown();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alert_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

            startActivityForResult(intent, SETTINGS_INFO);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_INFO) {
            updateAlarmCountdown();
        }
    }

    private void updateAlarmCountdown() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String alarmCountdownSetting = sharedPreferences.getString("alarm_set_countdown", "5");

        alarmCountdownValue = Byte.parseByte(alarmCountdownSetting);

        alarmCountdownText.setText(alarmCountdownSetting);

    }
}
