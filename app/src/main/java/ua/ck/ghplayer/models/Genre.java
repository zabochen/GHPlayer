package ua.ck.ghplayer.models;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Genre {
    //Constants for Genre query
    public static final String[] projection = {
            MediaStore.Audio.Genres.NAME
    };

    public static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    public static final Uri INTERNAL_CONTENT_URI = MediaStore.Audio.Genres.INTERNAL_CONTENT_URI;

    //Variables
    private String name;

    public Genre(String name){
        this.name=name;
    }

    public Genre(Cursor cursor){
        this.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
    }

    public String getName() {
        return name;
    }
}
