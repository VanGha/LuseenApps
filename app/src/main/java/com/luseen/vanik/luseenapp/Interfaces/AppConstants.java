package com.luseen.vanik.luseenapp.Interfaces;


public interface AppConstants {

    String TITTLE_REGISTER = "Регистрация";
    String TITTLE_LOGIN = "             Вход            ";
    String TITTLE_RECOVERY_PASSWORD = "Восстановить пароль";

    // Fragment Tags

    String TAG_MENU_NEWS = "news_fragment";
    String TAG_MENU_PUBLICATIONS = "publications_fragment";
    String TAG_MENU_MY_PUBLICATIONS = "my_publications_fragment";

    // Fragment Tags

    // Spinner

    String TYPE_SPECIALITY = "speciality";
    String TYPE_RANK = "rank";

    // Spinner

    //Intent

    String IS_LOGGED = "is_logged";
    String LOGGED_USER_EMAIL = "logged_user";
    String SETTINGS_ACTIVITY_KEY = "settings_key";

    String POST_IDS = "post_ids";
    String POST_SIZE = "post_sizes";
    String POST_POST_COMMENTS_SIZE = "post_comment_size";

    // Intent

    // Shared Preferences

    String LOGGED_USER_SHARED_PREFERENCE = "LoggedUser";

    String NOTIFICATION_CHECKER_SHARED_PREFERENCE = "NotChecker";
    String NOTIFICATION_POST_COMMENTS_CHECKED = "is_checked_nots";
    String NOTIFICATION_POST_ID = "not_post_id";

    String SETTINGS_SHARED_PREFERENCES = "SettingsSP";
    String SETTINGS_IS_IN_USE_BACKGROUND_PROCESSES = "is_back_process";


    // Shared Preferences

}
