package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Artist {
    //Constants for Artist query
    public static final String[] projection = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Artists.INTERNAL_CONTENT_URI;

    //Variables
    private int id;
    private String artistKey;
    private String artist;
    private int numberOfAlbums;
    private int numberOfTracks;

    public Artist(int id, String artistKey, String artist, int numberOfAlbums, int numberOfTracks) {
        this.id = id;
        this.artistKey = artistKey;
        this.artist = artist;
        this.numberOfAlbums = numberOfAlbums;
        this.numberOfTracks = numberOfTracks;
    }

    public Artist(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
        this.artistKey = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST_KEY));
        this.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
        this.numberOfAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        this.numberOfTracks = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
    }

    public int getId() {
        return id;
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
