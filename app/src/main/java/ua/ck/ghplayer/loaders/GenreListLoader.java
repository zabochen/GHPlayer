package ua.ck.ghplayer.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

public class GenreListLoader extends CursorLoader {

    private Context context;
    private Uri uriExternalContent = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    private String[] projection = {
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
    };
    private String selection = MediaStore.Audio.Genres.NAME + " != ''";

    public GenreListLoader(Context context) {
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
