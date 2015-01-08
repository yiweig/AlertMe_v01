package com.yiweigao.alertme;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by yiweigao on 1/8/15.
 */
public class Alarm {

    private byte countdown;
    private TextView countdownDisplay;
    private Activity activity;

    public Alarm(Activity mainActivity, byte passedCountdown) {
        this.activity = mainActivity;
        this.countdown = passedCountdown;
        this.countdownDisplay = (TextView) this.activity.findViewById(R.id.alarm_countdown_text);
    }


}
