package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Playlist {
    //Constants for Playlist query
    public static final String[] projection = {
            MediaStore.Audio.Playlists.NAME
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI;

    //Variables
    private String name;

    public Playlist(String name){
        this.name=name;
    }

    public Playlist(Cursor cursor){
        this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME));
    }

    public String getName() {
        return name;
    }
}

