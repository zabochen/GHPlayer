package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Track;
import ua.ck.ghplayer.utils.ImageUtils;

public class ArtistTrackList {

    private static ArtistTrackList instance;
    private static ArrayList<Track> artistTrackList;
    private static ArrayList<Track> saveArtistTrackList;

    private ArtistTrackList() {
    }

    public static ArtistTrackList getInstance() {
        if (instance == null) {
            instance = new ArtistTrackList();
        }
        return instance;
    }

    public void setArtistTrackList(Context context, Cursor cursor) {
        cursor.moveToFirst();
        artistTrackList = new ArrayList<>();
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
            artistTrackList.add(track);
        } while (cursor.moveToNext());
    }

    public ArrayList<Track> getArtistTrackList() {
        return artistTrackList != null ? artistTrackList : null;
    }

    public void saveArtistTrackList() {
        saveArtistTrackList = new ArrayList<>();
        saveArtistTrackList.addAll(artistTrackList);
    }

    public ArrayList<Track> getSaveArtistTrackList() {
        return saveArtistTrackList != null ? saveArtistTrackList : null;
    }

}
