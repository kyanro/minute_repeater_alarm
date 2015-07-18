package com.example.kyanro.minite_repeat_timer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Long> vibratePattern = getVibratePattern(Calendar.getInstance());
        long[] vibratePatternArray = new long[vibratePattern.size()];
        for (int i = 0; i < vibratePattern.size(); i++) {
            vibratePatternArray[i] = vibratePattern.get(i);
        }

        Log.d("log", vibratePattern.toString());

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText("Alarm text")
                .setSubText("sub title")
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setVibrate(vibratePatternArray)
                .build();

        NotificationManagerCompat.from(this).notify(1, notification);
    }

    private static final long SEPARATE_DURATION = 1000;

    private List<Long> getVibratePattern(Calendar alarmTime) {
        int hour = alarmTime.get(Calendar.HOUR);
        int minuteTens = alarmTime.get(Calendar.MINUTE) / 10;
        int minuteOnes = alarmTime.get(Calendar.MINUTE) % 10;

        List<Long> vibratePattern = new ArrayList<>();
        addVibratePattern(vibratePattern, hour, 500);
        vibratePattern.add(SEPARATE_DURATION);
        vibratePattern.add(0l);
        addVibratePattern(vibratePattern, minuteTens, 300);
        vibratePattern.add(SEPARATE_DURATION);
        vibratePattern.add(0l);
        addVibratePattern(vibratePattern, minuteOnes, 150);

        return vibratePattern;
    }

    private List<Long> addVibratePattern(List<Long> outPattern, int num, long duration) {
        for (int i = 0; i < num; i++) {
            outPattern.add(duration);
            outPattern.add(duration);
        }
        return outPattern;
    }
}
