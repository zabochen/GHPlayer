package ua.ck.ghplayer.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

public class ArtistTrackListLoader extends CursorLoader {

    private Context context;
    private Uri uriExternalContent = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] projection = {
            MediaStore.Audio.Genres.Members._ID,
            MediaStore.Audio.Genres.Members.TITLE,
            MediaStore.Audio.Genres.Members.ARTIST,
            MediaStore.Audio.Genres.Members.ALBUM,
            MediaStore.Audio.Genres.Members.ALBUM_ID,
            MediaStore.Audio.Genres.Members.DURATION
    };
    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND " +
            MediaStore.Audio.Media.ARTIST_ID + "=?";
    String[] selectionArgs;

    public ArtistTrackListLoader(Context context, long artistId) {
        super(context);
        this.context = context;
        selectionArgs = new String[]{String.valueOf(artistId)};
    }

    @Override
    public Cursor loadInBackground() {

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uriExternalContent, projection, selection, selectionArgs, null);
        return cursor;
    }

}
