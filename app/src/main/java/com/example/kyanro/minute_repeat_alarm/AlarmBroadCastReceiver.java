package com.example.kyanro.minute_repeat_alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    public static final String EXTRA_PATTERN = "extra_pattern";

    /** @return このレシーバーが受け取るための PendingIntent を返却する */
    public static PendingIntent createPendingIntent(Context c, long[] vibratePattern) {
        Intent intent = new Intent(c, AlarmBroadCastReceiver.class);
        intent.putExtra(EXTRA_PATTERN, vibratePattern);

        return PendingIntent.getBroadcast(c, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long[] vibratePattern = intent.getLongArrayExtra(EXTRA_PATTERN);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText("Alarm text")
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0))
                .setVibrate(vibratePattern)
                .build();

        NotificationManagerCompat.from(context).notify(1, notification);

        Log.d("log", Arrays.toString(vibratePattern));

    }
}
