package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Album {
    //Constants for Album query
    public static final String[] projection = {
            MediaStore.Audio.Albums.ALBUM_KEY,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR,
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Albums.INTERNAL_CONTENT_URI;

    //Variables
    private String albumKey;
    private String album;
    private String artist;
    private String albumArt;
    private int numberOfSongs;
    private int firstYear;

    public Album(String albumKey, String album, String artist, String albumArt, int numberOfSongs, int firstYear) {
        this.albumKey = albumKey;
        this.album = album;
        this.artist = artist;
        this.numberOfSongs = numberOfSongs;
        this.firstYear = firstYear;
    }

    public Album(Cursor cursor) {
        this.albumKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_KEY));
        this.album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
        this.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
        this.albumArt = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
        this.numberOfSongs = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
        this.firstYear = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.FIRST_YEAR));
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public int getFirstYear() {
        return firstYear;
    }

}
