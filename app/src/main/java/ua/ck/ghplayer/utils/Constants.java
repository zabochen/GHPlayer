package ua.ck.ghplayer.utils;

import android.content.Context;

public class Constants {

    // ---------------------------------------------------------------------------------------------
    // Sender's ID
    // ---------------------------------------------------------------------------------------------

    public static final int MINI_PLAYER_SENDER_ID = 0;
    public static final int PLAYER_SENDER_ID = 1;
    public static final int NOTIFICATION_PLAYER_SENDER_ID = 2;
    public static final int BROADCAST_SENDER_ID = 3;
    public static final int MUSIC_SERVICE_SENDER_ID = 4;
    public static final int MAIN_ACTIVITY_SENDER_ID = 5;

    // ---------------------------------------------------------------------------------------------
    // Track List's ID
    // ---------------------------------------------------------------------------------------------

    // Intent Key
    public static final String TRACK_LIST_ID_KEY = "track_list_id";

    // Track List's ID
    public static final int MAIN_TRACK_LIST_ID = 0;
    public static final int ALBUM_TRACK_LIST_ID = 1;
    public static final int ARTIST_ALBUM_TRACK_LIST_ID = 2;
    public static final int GENRE_TRACK_LIST_ID = 3;
    public static final int FAVORITE_TRACK_LIST_ID = 4;

    // ---------------------------------------------------------------------------------------------
    // Loader's ID
    // ---------------------------------------------------------------------------------------------

    public static final int MAIN_TRACK_LIST_LOADER_ID = 1;
    public static final int ALBUM_LIST_LOADER_ID = 2;
    public static final int ALBUM_TRACK_LIST_LOADER_ID = 3;
    public static final int ARTIST_LIST_LOADER_ID = 4;
    public static final int ARTIST_ALBUM_LIST_LOADER_ID = 5;
    public static final int ARTIST_ALBUM_TRACK_LIST_LOADER_ID = 6;
    public static final int GENRE_LIST_LOADER_ID = 7;
    public static final int GENRE_TRACK_LIST_LOADER_ID = 8;
    public static final int FAVORITE_LIST_LOADER_ID = 9;
    public static final int FAVORITE_TRACK_LIST_LOADER_ID = 10;

    // ---------------------------------------------------------------------------------------------
    // TrackList's - Activity Result
    // ---------------------------------------------------------------------------------------------

    public static final String ACTIVITY_RESULT_ITEM_ID_INTENT_KEY = "activity_result_item_id";
    public static final String ACTIVITY_RESULT_ITEM_POSITION_INTENT_KEY = "activity_result_item_position_id";
    public static final String ACTIVITY_RESULT_TRACK_LIST_ID_INTENT_KEY = "activity_result_track_list_id";
    public static final String ACTIVITY_RESULT_TRACK_POSITION_INTENT_KEY = "activity_result_track_position";
    public static final String ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY = "activity_result_status_button_pause";

    // ---------------------------------------------------------------------------------------------
    // Notification Player
    // ---------------------------------------------------------------------------------------------

    // Notification Player ID
    public static final int NOTIFICATION_PLAYER_ID = 8;

    // Notification
    public static final String NOTIFICATION_PLAYER_START = "ua.ck.player.notification.start";
    public static final String NOTIFICATION_PLAYER_STOP = "ua.ck.player.notification.stop";

    // Notification Player Buttons
    public static final String NOTIFICATION_PLAYER_BUTTON_EMPTY = "ua.ck.player.notification.button.empty";
    public static final String NOTIFICATION_PLAYER_BUTTON_PREVIOUS = "ua.ck.player.notification.button.previous";
    public static final String NOTIFICATION_PLAYER_BUTTON_PLAY = "ua.ck.player.notification.button.play";
    public static final String NOTIFICATION_PLAYER_BUTTON_PAUSE = "ua.ck.player.notification.button.pause";
    public static final String NOTIFICATION_PLAYER_BUTTON_STOP = "ua.ck.player.notification.button.stop";
    public static final String NOTIFICATION_PLAYER_BUTTON_NEXT = "ua.ck.player.notification.button.next";

    // ---------------------------------------------------------------------------------------------
    // Mini Player
    // ---------------------------------------------------------------------------------------------

    public static final String MINI_PLAYER_GONE_KEY = "mini_player_gone";

    // Current TrackList & Track
    public static final String MINI_PLAYER_TRACK_LIST_ID_KEY = "mini_player_track_list_id";
    public static final String MINI_PLAYER_TRACK_POSITION_KEY = "mini_player_track_position";

    // Mini Player Buttons
    public static final int MINI_PLAYER_BUTTON_PREVIOUS = 0;
    public static final int MINI_PLAYER_BUTTON_PLAY = 1;
    public static final int MINI_PLAYER_BUTTON_PAUSE = 2;
    public static final int MINI_PLAYER_BUTTON_STOP = 3;
    public static final int MINI_PLAYER_BUTTON_NEXT = 4;

    // ---------------------------------------------------------------------------------------------
    // Player
    // ---------------------------------------------------------------------------------------------

    // Player Status
    public static final int PLAYER_STATUS_START = 0;
    public static final int PLAYER_STATUS_STOP = 1;

    // Current TrackList & Track
    public static final String PLAYER_TRACK_LIST_ID_KEY = "player_track_list_id";
    public static final String PLAYER_TRACK_POSITION_KEY = "player_track_position";

    // Player Buttons
    public static final int PLAYER_BUTTON_PREVIOUS = 0;
    public static final int PLAYER_BUTTON_PLAY = 1;
    public static final int PLAYER_BUTTON_PAUSE = 2;
    public static final int PLAYER_BUTTON_NEXT = 3;

    // Player Update
    public static final String PLAYER_UPDATE_TRACK_CURRENT_DURATION = "player_update_track_current_duration";
    public static final String PLAYER_UPDATE_TRACK_TOTAL_DURATION = "player_update_track_total_duration";
    public static final long PLAYER_PROGRESS_BAR_UPDATE_TIME_INTERVAL = 100;    // milliseconds

    // ---------------------------------------------------------------------------------------------
    // Button Pause Visible Status
    // ---------------------------------------------------------------------------------------------

    public static final String BUTTON_PAUSE_VISIBLE_STATUS = "button_pause_visible_status";

    //----------------------------------------------------------------------------------------------
    // Preferences
    //----------------------------------------------------------------------------------------------

    public static final String PREFERENCES_FILE_NAME = "ghplayerPreferences";
    public static final int PREFERENCES_FILE_MODE = Context.MODE_PRIVATE;
    public static final String PREFERENCES_KEY_INITIALIZATION = "initialization";
    public static final String PREFERENCES_KEY_SORT_TRACK_LIST = "sortTrackList";

    //----------------------------------------------------------------------------------------------
    // Sorting TrackList
    //----------------------------------------------------------------------------------------------

    public static final int SORT_TRACK_LIST_ALBUM = 0;
    public static final int SORT_TRACK_LIST_ARTIST = 1;
    public static final int SORT_TRACK_LIST_TITLE = 2;
    public static final int SORT_TRACK_LIST_ALBUM_REVERSE = 3;
    public static final int SORT_TRACK_LIST_ARTIST_REVERSE = 4;
    public static final int SORT_TRACK_LIST_TITLE_REVERSE = 5;


}