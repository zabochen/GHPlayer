package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Artist {
    //Constants for Artist query
    public static final String[] projection = {
            MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Artists.INTERNAL_CONTENT_URI;

    //Variables
    private String artistKey;
    private String artist;
    private int numberOfAlbums;
    private int numberOfTracks;

    public Artist(String artistKey, String artist, int numberOfAlbums, int numberOfTracks) {
        this.artistKey = artistKey;
        this.artist = artist;
        this.numberOfAlbums = numberOfAlbums;
        this.numberOfTracks = numberOfTracks;
    }

    public Artist(Cursor cursor){
        this.artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
        this.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
        this.numberOfAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        this.numberOfTracks = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
    }

    public String getArtistKey() {
        return artistKey;
    }

    public String getArtist() {
        return artist;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

}
