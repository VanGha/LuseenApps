package com.luseen.vanik.luseenapp.Classes;


public class Settings {

    private static boolean isInUseBackgroundProcesses;

    public static boolean isInUseBackgroundProcesses() {
        return isInUseBackgroundProcesses;
    }

    public static void setIsInUseBackgroundProcesses(boolean isInUseBackgroundProcesses) {
        Settings.isInUseBackgroundProcesses = isInUseBackgroundProcesses;
    }

}
