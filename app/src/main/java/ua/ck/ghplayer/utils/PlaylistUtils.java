package ua.ck.ghplayer.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import ua.ck.ghplayer.loaders.CustomTrackListLoader;
import ua.ck.ghplayer.loaders.FavoriteTrackListLoader;

public class PlaylistUtils {

    private static Uri favoritePlaylistUri;
    private static long favoritePlaylistId;

    public static long getFavoritePlaylistId() {
        return favoritePlaylistId;
    }

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

        // Create favorite playlist if it doesn't exist
        if (cursor == null || !cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Audio.Playlists.NAME, "Favorites");
            favoritePlaylistUri = context.getContentResolver().insert(MediaStore.Audio.Playlists.getContentUri("external"), contentValues);
        } else {
            favoritePlaylistId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
            favoritePlaylistUri = MediaStore.Audio.Playlists.Members.getContentUri("external", favoritePlaylistId);
        }
    }

    public static void addTrackToFavorite(Context context, long trackId) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", PlaylistUtils.favoritePlaylistId);

        // Checking the existence of a track on the Favorite PlayList
        boolean trackCheck;
        String[] trackCheckProjection = new String[]{MediaStore.Audio.Playlists.Members.AUDIO_ID};
        String trackCheckSelection = MediaStore.Audio.Playlists.Members.AUDIO_ID + " = ?";
        String[] trackCheckSelectionArgs = {String.valueOf(trackId)};

        // Find by Track ID
        Cursor trackCheckCursor = contentResolver.query(uri, trackCheckProjection, trackCheckSelection, trackCheckSelectionArgs, null);
        if (trackCheckCursor != null && trackCheckCursor.moveToFirst()) {
            trackCheckCursor.close();
            // True - Track exist
            trackCheck = true;
            Toast.makeText(context, "Already Added To Favorites", Toast.LENGTH_SHORT).show();
        } else {
            trackCheck = false;
        }

        // Add Track to Favorite
        if (!trackCheck) {
            String[] projection = new String[]{MediaStore.Audio.Playlists.Members.PLAY_ORDER};
            String sortOrder = MediaStore.Audio.Playlists.Members.PLAY_ORDER + " DESC";

            // Get Last Track Position
            int trackPosition = 0;
            Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);
            if (cursor != null && cursor.moveToFirst()) {
                trackPosition = cursor.getInt(0) + 1;
                cursor.close();
            }

            // Add Track
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, trackId);
            contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, trackPosition);
            contentResolver.insert(uri, contentValues);
            Toast.makeText(context, "Added To Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteTrackFromFavorite(Context context, long trackId) {
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", PlaylistUtils.favoritePlaylistId);
        String trackSelection = MediaStore.Audio.Playlists.Members._ID + " = ?";
        String[] trackSelectionArgs = {String.valueOf(trackId)};

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(uri, trackSelection, trackSelectionArgs);
        Toast.makeText(context, "Removed From Favorites", Toast.LENGTH_SHORT).show();
    }

    public static void AddAlbumTracksToFavorite(Context context, long albumId) {
        //Get all tracks from album
        Cursor albumTracksCursor = new CustomTrackListLoader(context, "ALBUM", albumId, null).loadInBackground();

        if (albumTracksCursor != null && albumTracksCursor.moveToFirst()) {
            albumTracksCursor.moveToFirst();

            long trackId;

            do {
                trackId = albumTracksCursor.getLong(albumTracksCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                if (!isTrackInFavorite(context, trackId)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, trackId);
                    contentValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, getLastFavoriteTrackPosition(context));
                    context.getContentResolver().insert(getFavoritePlaylistUri(), contentValues);

                }

            } while (albumTracksCursor.moveToNext());

            albumTracksCursor.close();
            Toast.makeText(context, "Album added to favorites", Toast.LENGTH_LONG).show();
        }

    }

    public static boolean isTrackInFavorite(Context context, long trackId) {
        String[] trackProjection = new String[]{MediaStore.Audio.Playlists.Members.AUDIO_ID};
        String trackSelection = MediaStore.Audio.Playlists.Members.AUDIO_ID + " = ?";
        String[] trackSelectionArgs = {String.valueOf(trackId)};

        Cursor trackCursor = context.getContentResolver().query(getFavoritePlaylistUri(), trackProjection, trackSelection, trackSelectionArgs, null);
        if (trackCursor != null && trackCursor.moveToFirst()) {
            trackCursor.close();
            return true;
        } else {
            return false;
        }
    }

    private static int getLastFavoriteTrackPosition(Context context) {

        String[] projection = new String[]{MediaStore.Audio.Playlists.Members.PLAY_ORDER};
        String sortOrder = MediaStore.Audio.Playlists.Members.PLAY_ORDER + " DESC";

        // Get Last Track Position
        int trackPosition = 0;
        Cursor cursor = context.getContentResolver().query(getFavoritePlaylistUri(), projection, null, null, sortOrder);
        if (cursor != null && cursor.moveToFirst()) {
            trackPosition = cursor.getInt(0) + 1;
            cursor.close();
        }
        return trackPosition;
    }

}
