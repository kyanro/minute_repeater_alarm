package com.example.kyanro.minute_repeat_alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TimePickerDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar bootTime = Calendar.getInstance();

        mDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            List<Long> vibratePatternList = getVibratePattern(hourOfDay, minute);
            long[] vibratePattern = new long[vibratePatternList.size()];
            for (int i = 0; i < vibratePatternList.size(); i++) {
                vibratePattern[i] = vibratePatternList.get(i);
            }

            PendingIntent alarmIntent =
                    AlarmBroadCastReceiver.createPendingIntent(MainActivity.this, vibratePattern);

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar alarmTime = Calendar.getInstance();
            alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            alarmTime.set(Calendar.MINUTE, minute);
            alarmTime.set(Calendar.SECOND, 0);

            Calendar now = Calendar.getInstance();
            if (alarmTime.getTimeInMillis() < now.getTimeInMillis()) {
                alarmTime.add(Calendar.DAY_OF_MONTH, 1);
            }

            am.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), alarmIntent);

            SimpleDateFormat sdf = new SimpleDateFormat("M/d H:m");

            Toast.makeText(MainActivity.this,
                    "set: " + sdf.format(alarmTime.getTime()), Toast.LENGTH_LONG).show();

            MainActivity.this.finish();

        }, bootTime.get(Calendar.HOUR_OF_DAY), bootTime.get(Calendar.MINUTE), true);
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
