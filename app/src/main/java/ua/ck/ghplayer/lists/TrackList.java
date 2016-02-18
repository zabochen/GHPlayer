package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Track;

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

    public void setTrackList(Cursor cursor) {
        cursor.moveToFirst();
        trackList = new ArrayList<>();
        do {
            // Get The Value Of Fields
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Track.TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(Track.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(Track.ALBUM));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(Track.DURATION));

            // Add Object To ArrayList
            Track track = new Track(title, artist, album, duration);
            trackList.add(track);
        } while (cursor.moveToNext());
    }

    public ArrayList<Track> getTrackList() {
        return trackList != null ? trackList : null;
    }

    public void clearTrackList() {
        if (trackList != null) {
            trackList.clear();
        }
    }

}
