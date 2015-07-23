package com.nix.dimablyznyuk.student.contacts.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nix.dimablyznyuk.student.contacts.MainActivity;
import com.nix.dimablyznyuk.student.contacts.R;
import com.nix.dimablyznyuk.student.contacts.contenprovider.ContactsContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dima Blyznyuk on 22.07.15.
 */
public class BirthdayNotification extends BroadcastReceiver {

    private String TAG = "BirthdayNotification";
    private NotificationManager nm;
    private Context ctx;
    private int i=1;

    @Override
    public void onReceive(Context context, Intent intent) {

        ctx = context;
        List<String> nameList = getNameForTodayBirthday();
        if (!nameList.isEmpty()) {
            for (String name : nameList) {
                sendNotification(name);
            }
        }
    }

    List<String> getNameForTodayBirthday() {

        List<String> nameList = new ArrayList<String>();
        Cursor cursor = ctx.getContentResolver().query(ContactsContentProvider
                        .CONTACT_CONTENT_URI, null, null, null, null);
        cursor.getCount();
        Log.d(TAG, "BirthdayNotification cursor= " + cursor.getCount());
        String name;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            name = cursor.getString(0);
            nameList.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return nameList;
    }

    void sendNotification(String name) {

        PendingIntent pendingIntent = PendingIntent.getActivity(
                ctx,
                0,
                new Intent(),  //Dummy Intent do nothing
                0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.pie_ic)
                        .setContentTitle(ctx.getString(R.string.today_is)
                                + name + (ctx.getString(R.string.s_birthday)))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(i++, builder.build());

    }
}

