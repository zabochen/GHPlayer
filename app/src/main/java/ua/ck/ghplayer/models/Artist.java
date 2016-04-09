package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import ua.ck.ghplayer.utils.ArtistInfoUtils;

public class Artist {
    //Constants for Artist query
    public static final String[] projection = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Artists.INTERNAL_CONTENT_URI;

    //Variables
    private long id;
    private String artist;
    private int numberOfAlbums;
    private int numberOfTracks;
    private String artistArtUrl;


    public Artist(long id, String artist, int numberOfAlbums, int numberOfTracks, String artistArtUrl) {
        this.id = id;
        this.artist = artist;
        this.numberOfAlbums = numberOfAlbums;
        this.numberOfTracks = numberOfTracks;
        this.artistArtUrl = artistArtUrl;
    }

    public Artist(Cursor cursor, String artistArtUrl) {
        this.id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID));
        this.artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
        this.numberOfAlbums = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
        this.numberOfTracks = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
        this.artistArtUrl = artistArtUrl;
    }

    public long getId() {
        return id;
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

    public String getArtistArtUrl() {
        return artistArtUrl;
    }
}
