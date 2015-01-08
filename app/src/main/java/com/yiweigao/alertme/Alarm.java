package com.yiweigao.alertme;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by yiweigao on 1/8/15.
 */
public class Alarm {

    private byte countdown;
    private int secondsLeft = 0;
    private TextView countdownDisplay;
    private Activity activity;

    public Alarm(Activity mainActivity, byte passedCountdown) {
        this.activity = mainActivity;
        this.countdown = passedCountdown;
        this.countdownDisplay = (TextView) this.activity.findViewById(R.id.alarm_countdown_text);
    }

    public void startCountdown() {

        // countdown fix from
        // http://stackoverflow.com/a/6811744/1470257
        new CountDownTimer(countdown * 1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (Math.round((float) millisUntilFinished / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((int) millisUntilFinished / 1000.0f);
                    countdownDisplay.setText(Integer.toString(secondsLeft));
                }
            }

            @Override
            public void onFinish() {
                countdownDisplay.setText("done");
            }

            //            @Override
//            public void onTick(long millisUntilFinished) {
//                countdownDisplay.setText(Long.toString(millisUntilFinished / 1000));
//            }
//
//            @Override
//            public void onFinish() {
//                countdownDisplay.setText("done");
//            }
        }.start();

    }


}
