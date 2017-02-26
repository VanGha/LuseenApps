package com.luseen.vanik.luseenapp.Notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.luseen.vanik.luseenapp.Activities.MainActivity;
import com.luseen.vanik.luseenapp.R;

public class NotificationPostMessage extends BroadcastReceiver {

    public static void notify(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
        notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
        notificationBuilder.setSmallIcon(R.drawable.luseen_icon);
        notificationBuilder.setContentTitle(context.getResources().getString(R.string.new_comment_title));
        notificationBuilder.setContentText(context.getResources().getString(R.string.has_a_new_comment));
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setContentIntent(PendingIntent.getActivity(context, 1,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        notificationBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        notify(context);

    }

}
