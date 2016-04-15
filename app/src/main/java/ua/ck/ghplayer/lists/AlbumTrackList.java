package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Track;
import ua.ck.ghplayer.utils.ImageUtils;

public class AlbumTrackList {

    private static AlbumTrackList instance;
    private static ArrayList<Track> albumTrackList;
    private static ArrayList<Track> saveAlbumTrackList;

    private AlbumTrackList() {
    }

    public static AlbumTrackList getInstance() {
        if (instance == null) {
            instance = new AlbumTrackList();
        }
        return instance;
    }

    public void setAlbumTrackList(Context context, Cursor cursor) {
        cursor.moveToFirst();
        albumTrackList = new ArrayList<>();
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
            albumTrackList.add(track);
        } while (cursor.moveToNext());
    }

    public ArrayList<Track> getAlbumTrackList() {
        return albumTrackList != null ? albumTrackList : null;
    }

    public void saveAlbumTrackList() {
        saveAlbumTrackList = new ArrayList<>();
        saveAlbumTrackList.addAll(albumTrackList);
    }

    public ArrayList<Track> getSaveAlbumTrackList() {
        return saveAlbumTrackList != null ? saveAlbumTrackList : null;
    }

}
