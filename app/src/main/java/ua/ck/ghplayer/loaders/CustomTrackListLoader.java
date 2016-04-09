package ua.ck.ghplayer.loaders;


import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class CustomTrackListLoader extends CursorLoader {
    private Context context;
    private String choiceMode;
    private long id;
    private String[] selectionArgs;

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

    public CustomTrackListLoader(Context context) {
        super(context);
        this.context = context;
    }

    public CustomTrackListLoader(Context context, String choiceMode, long id) {
        this(context);
        this.choiceMode = choiceMode;
        this.id = id;
    }

    @Override
    public Cursor loadInBackground() {
        selectionArgs = new String[]{String.valueOf(id)};
        switch (choiceMode) {
            case "ALBUM":
                selection = selection +" AND " + MediaStore.Audio.Media.ALBUM_ID + "=?";
                break;
            case "ARTIST":
                selection = selection +" AND " + MediaStore.Audio.Media.ARTIST_ID + "=?";
                break;
            case "PLAYLIST":
                uriExternalContent = MediaStore.Audio.Playlists.Members.getContentUri("external", id);
                break;
        }

        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.query(uriExternalContent, projection, selection, selectionArgs, null);
    }
}
