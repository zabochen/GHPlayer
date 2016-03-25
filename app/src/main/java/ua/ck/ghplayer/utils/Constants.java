package ua.ck.ghplayer.utils;

import android.content.Context;

public class Constants {

    // ---------------------------------------------------------------------------------------------
    // Notification Player
    // ---------------------------------------------------------------------------------------------

    // Notification
    public static final String NOTIFICATION_PLAYER_START = "ua.ck.player.notification.start";
    public static final String NOTIFICATION_PLAYER_STOP = "ua.ck.player.notification.stop";

    // Buttons Click
    public static final String NOTIFICATION_PLAYER_BUTTON_PREVIOUS = "ua.ck.player.notification.button.previous";
    public static final String NOTIFICATION_PLAYER_BUTTON_PLAY = "ua.ck.player.notification.button.play";
    public static final String NOTIFICATION_PLAYER_BUTTON_PAUSE = "ua.ck.player.notification.button.pause";
    public static final String NOTIFICATION_PLAYER_BUTTON_STOP = "ua.ck.player.notification.button.stop";
    public static final String NOTIFICATION_PLAYER_BUTTON_NEXT = "ua.ck.player.notification.button.next";

    // ---------------------------------------------------------------------------------------------
    // Mini Player
    // ---------------------------------------------------------------------------------------------

    public static final String MINI_PLAYER_GONE_KEY = "miniPlayerGone";
    public static final String MINI_PLAYER_TRACK_LIST_ID_KEY = "miniPlayerTrackList";
    public static final String MINI_PLAYER_TRACK_POSITION_KEY = "miniPlayerTrackPosition";


    // ---------------------------------------------------------------------------------------------
    // Player
    // ---------------------------------------------------------------------------------------------

    // Player Status
    public static final int PLAYER_STATUS_START = 0;
    public static final int PLAYER_STATUS_STOP = 1;

    // ProgressBar Update
    public static final long PLAYER_PROGRESS_BAR_UPDATE_TIME_INTERVAL = 100;    // milliseconds

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

    // ---------------------------------------------------------------------------------------------
    // Events - Show TrackList
    // ---------------------------------------------------------------------------------------------

    public static final int SHOW_GENRE_TRACK_LIST = 0;
    public static final String SHOW_GENRE_TRACK_LIST_BUNDLE_KEY = "show_genre_track_list";

    // ---------------------------------------------------------------------------------------------
    // Events - Player Track List
    // ---------------------------------------------------------------------------------------------

    public static final String TRACK_LISTS_ID_KEY = "track_lists_id";

    public static final int MAIN_TRACK_LIST_ID = 0;
    public static final int ALBUM_TRACK_LIST_ID = 1;
    public static final int ARTIST_ALBUM_TRACK_LIST_ID = 2;
    public static final int GENRE_TRACK_LIST_ID = 3;
    public static final int PLAYLIST_TRACK_LIST_ID = 4;


}
