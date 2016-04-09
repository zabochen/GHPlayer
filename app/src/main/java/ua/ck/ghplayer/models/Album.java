package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Album {
    //Constants for Album query
    public static final String[] projection = {
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR,
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Albums.INTERNAL_CONTENT_URI;

    public static String selection = MediaStore.Audio.Media.ARTIST_ID + "=?";
    //MediaStore.Audio.Media.TITLE + " ASC"

    //Variables
    private long id;
    private String album;
    private String artist;
    private int numberOfSongs;
    private int firstYear;

    public Album(long id, String album, String artist, int numberOfSongs, int firstYear) {
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
    }

    public Album(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
        this.album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
        this.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
        this.numberOfSongs = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
        this.firstYear = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR));
    }

    public long getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public int getFirstYear() {
        return firstYear;
    }

    public Uri getAlbumCoverUri() {
        return Uri.parse("content://media/external/audio/albumart/" + id);
    }

}
