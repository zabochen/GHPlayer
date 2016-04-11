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
    private Uri uriExternalContent;
    private String[] projection = {
            MediaStore.Audio.Genres.Members._ID,
            MediaStore.Audio.Genres.Members.TITLE,
            MediaStore.Audio.Genres.Members.ARTIST,
            MediaStore.Audio.Genres.Members.ALBUM,
            MediaStore.Audio.Genres.Members.ALBUM_ID,
            MediaStore.Audio.Genres.Members.DURATION
    };
    private String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";

    public FavoriteTrackListLoader(Context context) {
        super(context);
        this.context = context;
        this.uriExternalContent = PlaylistUtils.getFavoritePlaylistUri();
    }

    @Override
    public Cursor loadInBackground() {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uriExternalContent, projection, selection, null, null);
        return cursor;
    }

}
