package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Track;
import ua.ck.ghplayer.utils.ImageUtils;

public class FavoriteTrackList {

    private static FavoriteTrackList instance;
    private static ArrayList<Track> favoriteTrackList;

    private FavoriteTrackList() {
    }

    public static FavoriteTrackList getInstance() {
        if (instance == null) {
            instance = new FavoriteTrackList();
        }
        return instance;
    }

    public void setFavoriteTrackList(Context context, Cursor cursor) {
        cursor.moveToFirst();
        favoriteTrackList = new ArrayList<>();
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
            favoriteTrackList.add(track);
        } while (cursor.moveToNext());
    }

    public ArrayList<Track> getFavoriteTrackList() {
        return favoriteTrackList != null ? favoriteTrackList : null;
    }
}
