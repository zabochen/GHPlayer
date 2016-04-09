package ua.ck.ghplayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

import io.realm.RealmConfiguration;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.PlaylistUtils;

public class Application extends android.app.Application {
    public static RealmConfiguration realmConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());

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

        //Realm database
        realmConfiguration = new RealmConfiguration.Builder(getApplicationContext()).name("default.realm").build();

        //Check for the existence of favorite playlist or create it
        PlaylistUtils.initFavoritePlaylist(getApplicationContext());

    }

}
