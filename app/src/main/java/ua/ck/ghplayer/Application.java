package ua.ck.ghplayer;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import android.content.Context;
import android.content.SharedPreferences;

import ua.ck.ghplayer.utils.Constants;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // Shared Preferences
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(Constants.PREFERENCES_FILE_NAME, Constants.PREFERENCES_FILE_MODE);
        SharedPreferences.Editor sharedPreferencesEditor;

        if (!sharedPreferences.contains(Constants.PREFERENCES_KEY_INITIALIZATION)) {
            sharedPreferencesEditor = sharedPreferences.edit();

            sharedPreferencesEditor.putBoolean(Constants.PREFERENCES_KEY_INITIALIZATION, true);
            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_TITLE);

            sharedPreferencesEditor.apply();
        }

    }

}
