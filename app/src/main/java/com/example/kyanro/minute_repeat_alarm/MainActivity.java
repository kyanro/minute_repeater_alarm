package com.example.kyanro.minute_repeat_alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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

    private TimePickerDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            List<Long> vibratePattern = getVibratePattern(hourOfDay, minute);
            long[] vibratePatternArray = new long[vibratePattern.size()];
            for (int i = 0; i < vibratePattern.size(); i++) {
                vibratePatternArray[i] = vibratePattern.get(i);
            }

            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Alarm")
                    .setContentText("Alarm text")
                    .setSubText("hour:" + hourOfDay % 12 + " minute:" + minute)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(MainActivity.this, 0, new Intent(), 0))
                    .setVibrate(vibratePatternArray)
                    .build();

            NotificationManagerCompat.from(MainActivity.this).notify(1, notification);

            Log.d("log", vibratePattern.toString());

        }, 0, 0, true);
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private static final long SEPARATE_DURATION = 1000;

    private List<Long> getVibratePattern(int hourOfDay, int minute) {
        int hour = hourOfDay % 12;
        int minuteTens = minute / 10;
        int minuteOnes = minute % 10;

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
