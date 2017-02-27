package com.luseen.vanik.luseenapp.Services;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckPostsAndCommentsUpdatesService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Context context = getApplicationContext();

        if (InternetConnection.hasInternetConnection(context)) {

            ParseQuery<LuseenPostComment> luseenPostCommentParseQueryOld = ParseQuery.getQuery(LuseenPostComment.class);
            luseenPostCommentParseQueryOld.countInBackground(new CountCallback() {
                @Override
                public void done(final int oldCount, ParseException e) {

                    if (e == null) {

                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {

                            @Override
                            public void run() {

                                final ParseQuery<LuseenPostComment> luseenPostCommentParseQueryNew = ParseQuery.getQuery(LuseenPostComment.class);
                                luseenPostCommentParseQueryNew.countInBackground(new CountCallback() {
                                    @Override
                                    public void done(final int newCount, ParseException e) {

                                        if (e == null) {

                                            ParseQuery<LuseenPosts> luseenPostsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
                                            luseenPostsParseQuery.countInBackground(new CountCallback() {
                                                @Override
                                                public void done(final int count, ParseException e) {

                                                    luseenPostCommentParseQueryNew.findInBackground(new FindCallback<LuseenPostComment>() {
                                                        @Override
                                                        public void done(List<LuseenPostComment> comments, ParseException e) {

                                                            SharedPreferences sharedPreferences =
                                                                    getSharedPreferences(AppConstants.NOTIFICATION_CHECKER_SHARED_PREFERENCE,
                                                                            MODE_PRIVATE);

                                                            for (int i = 0; i < count; i++) {

                                                                String checkedPostId = sharedPreferences.
                                                                        getString(String.valueOf(AppConstants.NOTIFICATION_POST_ID + i), "0");

                                                                if (!checkedPostId.equals("0")) {

                                                                    for (int j = 0; j < comments.size(); j++) {

                                                                        if (checkedPostId.equals(comments.get(j).getPostId()) && oldCount < newCount) {

                                                                            Intent toNotification = new Intent(context, NotificationCommentMessage.class);
                                                                            final PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, toNotification,
                                                                                    PendingIntent.FLAG_CANCEL_CURRENT);

                                                                            final Calendar calendar = Calendar.getInstance();
                                                                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }
                                                    });



                                                }
                                            });

                                            stopSelf();

                                        }

                                    }
                                });

                            }

                        }, 60000 * 2);

                    }

                }
            });

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
