package com.yiweigao.alertme;

import android.app.Activity;
import android.os.CountDownTimer;
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

    public void startCountdown() {

        new CountDownTimer(countdown * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countdownDisplay.setText(Long.toString(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                countdownDisplay.setText("done");
            }
        }.start();

    }


}
