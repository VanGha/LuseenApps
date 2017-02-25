package com.luseen.vanik.luseenapp.Services;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Notifications.NotificationCommentMessage;
import com.luseen.vanik.luseenapp.Parse.LuseenPostComment;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

public class NotificationService extends Service {

    private static String postId;

    private static List<LuseenPosts> oldPosts;
    private static List<LuseenPostComment> oldPostComments;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Context context = getApplicationContext();



        if (InternetConnection.hasInternetConnection(context)) {

            ParseQuery<LuseenPosts> luseenPostsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
            luseenPostsParseQuery.findInBackground(new FindCallback<LuseenPosts>() {
                @Override
                public void done(List<LuseenPosts> posts, ParseException e) {

                    if (e == null) {


                    }

                }
            });

            ParseQuery<LuseenPostComment> luseenPostCommentParseQuery = ParseQuery.getQuery(LuseenPostComment.class);
            luseenPostCommentParseQuery.findInBackground(new FindCallback<LuseenPostComment>() {
                @Override
                public void done(List<LuseenPostComment> postComments, ParseException e) {

                    if (e == null) {

                        for (int i = 0; i < postComments.size(); i++) {

                            if (oldPostComments.size() < postComments.size() &&
                                    postComments.get(i).getPostId().equals(postId)) {

                                Intent toNotification = new Intent(context, NotificationCommentMessage.class);
                                final PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, toNotification,
                                        PendingIntent.FLAG_CANCEL_CURRENT);

                                final Calendar calendar = Calendar.getInstance();
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent);

                            }

                        }

                    }

                }
            });

        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static void setPostId(String postId) {
        NotificationService.postId = postId;
    }

    public static void setPosts(List<LuseenPosts> posts) {
        NotificationService.oldPosts = posts;
    }

    public static void setPostComments(List<LuseenPostComment> oldPostComments) {
        NotificationService.oldPostComments = oldPostComments;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
