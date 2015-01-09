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

    private byte countdown;
    private int secondsLeft = 0;
    private int originalVolume;
    private TextView countdownDisplay;
    private Activity activity;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private MotionDetector motionDetector;

    public Alarm(Activity mainActivity, byte passedCountdown) {
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
                motionDetector.stopCalibration();
                countdownDisplay.setText("set");
            }
        }.start();

    }

    public void setTime(byte newTime) {
        this.countdown = newTime;
        this.countdownDisplay.setText(Byte.toString(this.countdown));
    }

    public void startAlarm() {

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        mediaPlayer.start();

    }

    public void stopAlarm() {
        mediaPlayer.stop();
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
    }

    public void registerSensorListener() {
        motionDetector.register();
    }

    public void unregisterSensorListener() {
        motionDetector.unregister();
    }


}
