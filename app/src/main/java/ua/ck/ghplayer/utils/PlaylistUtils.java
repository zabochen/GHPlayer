package ua.ck.ghplayer.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class PlaylistUtils {
    private static Uri favoritePlaylistUri;

    public static Uri getFavoritePlaylistUri() {
        return favoritePlaylistUri;
    }

    public static void initFavoritePlaylist(Context context) {
        String[] projection = {
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME
        };

        String selection = MediaStore.Audio.Playlists.NAME + "=?";
        String[] selectionArgs = new String[]{"Favorites"};


        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);


        //Create favorite playlist if it doesn't exist
        if (cursor == null || !cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Audio.Playlists.NAME, "Favorites");
            favoritePlaylistUri = context.getContentResolver().insert(MediaStore.Audio.Playlists.getContentUri("external"), contentValues);
        } else {
            long playlistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
            favoritePlaylistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        }
    }

    public void addTrackToFavorive() {

    }

    public void deleteTrackFromFavorite() {

    }

}
