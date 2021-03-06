package ua.ck.ghplayer.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

public class TrackListLoader extends CursorLoader {

    private Context context;
    private Uri uriExternalContent = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION
    };
    private String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";

    public TrackListLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uriExternalContent, projection, selection, null, null);
        return cursor;
    }

}
