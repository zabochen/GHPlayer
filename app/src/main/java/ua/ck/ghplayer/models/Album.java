package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


public class Album {

    private int album_id;
    private String album;
    private String artist;
    private int numsongs;
    private int minyear;

    // Constants
    //String[] projection for ContentResolver.query
    public static final String[] projection = {
            MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.FIRST_YEAR,
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Albums.INTERNAL_CONTENT_URI;

    public static final String ALBUM_ID ="album_id";
    public static final String ALBUM ="album";
    public static final String ARTIST ="artist";
    public static final String NUMBER_OF_SONGS="numsongs";
    public static final String FIRST_YEAR="minyear";

    Album(int album_id, String album, String artist, int numsongs, int minyear){
        this.album_id = album_id;
        this.album = album;
        this.artist = artist;
        this.numsongs = numsongs;
        this.minyear = minyear;
    }

    public Album getAlbumItemFromCursor(Cursor data){
        int album_id = data.getInt(data.getColumnIndexOrThrow(ALBUM_ID));
        String album = data.getString(data.getColumnIndexOrThrow(ALBUM));
        String artist = data.getString(data.getColumnIndexOrThrow(ARTIST));
        int numsongs = data.getInt(data.getColumnIndexOrThrow(NUMBER_OF_SONGS));
        int minyear = data.getInt(data.getColumnIndexOrThrow(FIRST_YEAR));

        return new Album(album_id,album,artist,numsongs,minyear);
    }

    public int getAlbum_id() {
        return album_id;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getNumsongs() {
        return numsongs;
    }

    public int getMinyear() {
        return minyear;
    }


}
