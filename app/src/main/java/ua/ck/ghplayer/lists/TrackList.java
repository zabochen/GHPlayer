package ua.ck.ghplayer.lists;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import ua.ck.ghplayer.comparators.TrackListComparator;
import ua.ck.ghplayer.models.Track;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.ImageUtils;

public class TrackList {

    private static TrackList instance;
    private static ArrayList<Track> trackList;

    private TrackList() {
    }

    public static TrackList getInstance() {
        if (instance == null) {
            instance = new TrackList();
        }
        return instance;
    }

    public void setTrackList(Context context, Cursor cursor) {
        cursor.moveToFirst();
        trackList = new ArrayList<>();
        do {
            // Get The Value Of Fields
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Track.ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Track.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(Track.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(Track.ALBUM));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(Track.ALBUM_ID));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(Track.DURATION));
            Uri albumArt = ImageUtils.getAlbumArt(context, albumId);

            // Add Object To ArrayList
            Track track = new Track(id, title, artist, album, albumId, albumArt, duration);
            trackList.add(track);
        } while (cursor.moveToNext());

        // Get Preferences Sorting
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES_FILE_NAME, Constants.PREFERENCES_FILE_MODE);

        switch (sharedPreferences.getInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_TITLE)) {
            case (Constants.SORT_TRACK_LIST_ALBUM):
                sortAlbum();
                break;
            case (Constants.SORT_TRACK_LIST_ALBUM_REVERSE):
                sortAlbumReverse();
                break;
            case (Constants.SORT_TRACK_LIST_ARTIST):
                sortArtist();
                break;
            case (Constants.SORT_TRACK_LIST_ARTIST_REVERSE):
                sortArtistReverse();
                break;
            case (Constants.SORT_TRACK_LIST_TITLE):
                sortTitle();
                break;
            case (Constants.SORT_TRACK_LIST_TITLE_REVERSE):
                sortTitleReverse();
                break;
        }
    }

    public ArrayList<Track> getTrackList() {
        return trackList != null ? trackList : null;
    }

    //----------------------------------------------------------------------------------------------
    // Sorting TrackList
    //----------------------------------------------------------------------------------------------

    public void sortAlbum() {
        Collections.sort(trackList, new TrackListComparator.SortAlbum());
    }

    public void sortAlbumReverse() {
        Collections.sort(trackList, new TrackListComparator.SortAlbum());
        Collections.reverse(trackList);
    }

    public void sortArtist() {
        Collections.sort(trackList, new TrackListComparator.SortArtist());
    }

    public void sortArtistReverse() {
        Collections.sort(trackList, new TrackListComparator.SortArtist());
        Collections.reverse(trackList);
    }

    public void sortTitle() {
        Collections.sort(trackList, new TrackListComparator.SortTitle());
    }

    public void sortTitleReverse() {
        Collections.sort(trackList, new TrackListComparator.SortTitle());
        Collections.reverse(trackList);
    }
}
