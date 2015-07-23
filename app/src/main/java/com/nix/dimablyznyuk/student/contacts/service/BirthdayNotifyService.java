package com.nix.dimablyznyuk.student.contacts.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.nix.dimablyznyuk.student.contacts.receiver.BirthdayNotification;

import java.util.Calendar;

/**
 * Created by Dima Blyznyuk on 22.07.15.
 */
public class BirthdayNotifyService extends Service {
    private static final String TAG = "BirthdayNotifyService";
    private AlarmManager am;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BirthdayNotifyService onCreate");
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                new Intent(this, BirthdayNotification.class), 0);

        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        return Service.START_STICKY;
    }
}
