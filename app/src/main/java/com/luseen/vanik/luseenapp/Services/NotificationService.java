package com.luseen.vanik.luseenapp.Services;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.luseen.vanik.luseenapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Context context = getApplicationContext();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Notification.Builder notificationBuilder = new Notification.Builder(context);
                notificationBuilder.setCategory(Notification.CATEGORY_EVENT);
                notificationBuilder.setColor(context.getResources().getColor(R.color.colorAccent));
                notificationBuilder.setSmallIcon(R.drawable.library_books);
                notificationBuilder.setContentTitle(context.getResources().getString(R.string.new_comment_title));
                notificationBuilder.setContentText(context.getResources().getString(R.string.has_a_new_comment));
                notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                notificationBuilder.setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());

            }
        }, 2000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
