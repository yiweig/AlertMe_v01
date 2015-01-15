package com.yiweigao.alertme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by yiweigao on 1/8/15.
 */
public class Alarm {

    private int countdown;
    private int timeout = 0;
    private int originalVolume;
    private int countdownSecondsLeft = 0;
    private int timeoutSecondsLeft = 0;
    private TextView countdownDisplay;
    private Activity activity;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private MotionDetector motionDetector;
    private CountDownTimer countdownTimer;
    private CountDownTimer timeoutTimer;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean message = intent.getBooleanExtra("hasMotion", false);

            if (message) {
                startAlarm();
                countdownDisplay.setText("triggered");
            }
        }
    };

    public Alarm(Activity mainActivity, int passedCountdown) {
        this.activity = mainActivity;
        this.countdown = passedCountdown;
        this.countdownDisplay = (TextView) mainActivity.findViewById(R.id.alarm_countdown_text);
        this.audioManager = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
        this.originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        this.mediaPlayer = MediaPlayer.create(mainActivity, R.raw.some_nights_fun);
        this.motionDetector = new MotionDetector(mainActivity.getApplicationContext());
    }

    public void startCountdown() {

        motionDetector.startCalibration();

        // countdown fix from
        // http://stackoverflow.com/a/6811744/1470257
        countdownTimer = new CountDownTimer(countdown * 1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (Math.round((float) millisUntilFinished / 1000.0f) != countdownSecondsLeft) {
                    countdownSecondsLeft = Math.round((int) millisUntilFinished / 1000.0f);
                    countdownDisplay.setText(Integer.toString(countdownSecondsLeft));
                }
            }

            @Override
            public void onFinish() {
                motionDetector.stopCalibration();
                countdownDisplay.setText("set");
                motionDetector.turnOn();
            }
        };
        countdownTimer.start();

    }

    public void setCountdown(int newCountdown) {
        countdown = newCountdown;
        countdownDisplay.setText(Integer.toString(this.countdown));
    }

    public void setTimeout(int newTimeout) {
        timeout = newTimeout;
    }

    private void playAlarm() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        mediaPlayer.start();
    }

    public void startAlarm() {

        if (timeout > 0) {
            timeoutTimer = new CountDownTimer(timeout * 1000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (Math.round((float) millisUntilFinished / 1000.0f) != timeoutSecondsLeft) {
                        timeoutSecondsLeft = Math.round((int) millisUntilFinished / 1000.0f);
                    }
                }

                @Override
                public void onFinish() {
                    playAlarm();
                }
            };
            timeoutTimer.start();
            return;
        }

        playAlarm();
    }

    public void stopAlarm() {
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
    }

    public void stopCountdown() {
        countdownTimer.cancel();
        countdownDisplay.setText("canceled");
    }

    public void stopTimeout() {
        timeoutTimer.cancel();
    }

    public void registerSensorListener() {
        motionDetector.register();
    }

    public void unregisterSensorListener() {
        motionDetector.unregister();
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }
}
