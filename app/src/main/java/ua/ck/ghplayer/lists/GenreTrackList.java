package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Track;
import ua.ck.ghplayer.utils.ImageUtils;

public class GenreTrackList {

    private static GenreTrackList instance;
    private static ArrayList<Track> genreTrackList;
    private static ArrayList<Track> saveGenreTrackList;

    private GenreTrackList() {
    }

    public static GenreTrackList getInstance() {
        if (instance == null) {
            instance = new GenreTrackList();
        }
        return instance;
    }

    public void setGenreTrackList(Context context, Cursor cursor) {
        cursor.moveToFirst();
        genreTrackList = new ArrayList<>();
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
            genreTrackList.add(track);
        } while (cursor.moveToNext());
    }

    public ArrayList<Track> getGenreTrackList() {
        return genreTrackList != null ? genreTrackList : null;
    }

    public void saveGenreTrackList() {
        saveGenreTrackList = new ArrayList<>();
        saveGenreTrackList.addAll(genreTrackList);
    }

    public ArrayList<Track> getSaveGenreTrackList() {
        return saveGenreTrackList != null ? saveGenreTrackList : null;
    }

}
