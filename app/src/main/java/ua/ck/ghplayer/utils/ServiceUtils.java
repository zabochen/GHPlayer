package ua.ck.ghplayer.utils;

import android.app.ActivityManager;
import android.content.Context;

import ua.ck.ghplayer.services.MusicService;

public class ServiceUtils {

    // Service status: TRUE - "Running", FALSE - "Stop"
    public static boolean musicServiceRunning(Context context, Class<MusicService> musicServiceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (musicServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
