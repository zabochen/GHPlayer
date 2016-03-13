package ua.ck.ghplayer.utils;

import android.content.Context;

public class Constants {

    //----------------------------------------------------------------------------------------------
    // Preferences
    //----------------------------------------------------------------------------------------------

    public static final String PREFERENCES_FILE_NAME = "ghplayerPreferences";
    public static final int PREFERENCES_FILE_MODE = Context.MODE_PRIVATE;
    public static final String PREFERENCES_KEY_INITIALIZATION = "initialization";
    public static final String PREFERENCES_KEY_SORT_TRACK_LIST = "sortTrackList";

    //----------------------------------------------------------------------------------------------
    // Sorting
    //----------------------------------------------------------------------------------------------

    // Sorting TrackList
    public static final int SORT_TRACK_LIST_ALBUM = 0;
    public static final int SORT_TRACK_LIST_ARTIST = 1;
    public static final int SORT_TRACK_LIST_TITLE = 2;
    public static final int SORT_TRACK_LIST_ALBUM_REVERSE = 3;
    public static final int SORT_TRACK_LIST_ARTIST_REVERSE = 4;
    public static final int SORT_TRACK_LIST_TITLE_REVERSE = 5;


}
