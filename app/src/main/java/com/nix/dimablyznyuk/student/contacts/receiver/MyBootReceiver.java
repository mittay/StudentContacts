package com.nix.dimablyznyuk.student.contacts.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nix.dimablyznyuk.student.contacts.service.BirthdayNotifyService;

/**
 * Created by Dima Blyznyuk on 22.07.15.
 */
public class MyBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, BirthdayNotifyService.class);
        context.startService(myIntent);

    }
}