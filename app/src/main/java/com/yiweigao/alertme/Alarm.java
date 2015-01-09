package com.yiweigao.alertme;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by yiweigao on 1/8/15.
 */
public class Alarm {

    private int countdown;
    private int timeout;
    private int originalVolume;
    private int countdownSecondsLeft;
    private int timeoutSecondsLeft;
    private TextView countdownDisplay;
    private Activity activity;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private MotionDetector motionDetector;
    private CountDownTimer countdownTimer;
    private CountDownTimer timeoutTimer;

    public Alarm(Activity mainActivity, int passedCountdown) {
        this.activity = mainActivity;
        this.countdown = passedCountdown;
        this.countdownDisplay = (TextView) mainActivity.findViewById(R.id.alarm_countdown_text);
        this.audioManager = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
        this.originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        this.mediaPlayer = MediaPlayer.create(mainActivity, R.raw.some_nights_fun);
        this.motionDetector = new MotionDetector(mainActivity.getApplicationContext());
        this.timeout = 0;
        this.countdownSecondsLeft = 0;
        this.timeoutSecondsLeft = 0;
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
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
    }

    public void stopCountdown() {
        countdownTimer.cancel();
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


}
