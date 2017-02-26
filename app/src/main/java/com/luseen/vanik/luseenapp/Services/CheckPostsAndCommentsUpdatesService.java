package com.luseen.vanik.luseenapp.Services;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Notifications.NotificationCommentMessage;
import com.luseen.vanik.luseenapp.Parse.LuseenPostComment;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

public class CheckPostsAndCommentsUpdatesService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Context context = getApplicationContext();

//        if (InternetConnection.hasInternetConnection(context)) {
//
//            ParseQuery<LuseenPosts> luseenPostsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
//            luseenPostsParseQuery.findInBackground(new FindCallback<LuseenPosts>() {
//                @Override
//                public void done(List<LuseenPosts> posts, ParseException e) {
//
//                    if (e == null) {
//
//
//                    }
//
//                }
//            });
//
//            ParseQuery<LuseenPostComment> luseenPostCommentParseQuery = ParseQuery.getQuery(LuseenPostComment.class);
//            luseenPostCommentParseQuery.findInBackground(new FindCallback<LuseenPostComment>() {
//                @Override
//                public void done(List<LuseenPostComment> postComments, ParseException e) {
//
//                    if (e == null) {
//
//                        Intent toNotification = new Intent(context, NotificationCommentMessage.class);
//                        final PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, toNotification,
//                                PendingIntent.FLAG_CANCEL_CURRENT);
//
//                        final Calendar calendar = Calendar.getInstance();
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);
//
//                    }
//                }
//            });
//        }

        Intent toNotification = new Intent(context, NotificationCommentMessage.class);
        final PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, toNotification,
                PendingIntent.FLAG_CANCEL_CURRENT);

        final Calendar calendar = Calendar.getInstance();
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
