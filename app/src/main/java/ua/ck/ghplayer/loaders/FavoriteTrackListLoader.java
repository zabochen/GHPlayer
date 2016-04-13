package ua.ck.ghplayer.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import ua.ck.ghplayer.utils.PlaylistUtils;

public class FavoriteTrackListLoader extends CursorLoader {

    private Context context;
    private long favoritePlaylistId = PlaylistUtils.getFavoritePlaylistId();
    private Uri uriExternalContent = MediaStore.Audio.Playlists.Members.getContentUri("external", favoritePlaylistId);
    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION
    };
    private String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
    private String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

    public FavoriteTrackListLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uriExternalContent, projection, selection, null, sortOrder);
        return cursor;
    }

}
