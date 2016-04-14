package ua.ck.ghplayer.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import ua.ck.ghplayer.models.Artist;

public class ArtistListLoader extends CursorLoader {
    Context context;

    public ArtistListLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(Artist.EXTERNAL_CONTENT_URI,Artist.projection, null, null, null);
    }
}
