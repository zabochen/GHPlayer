package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Track;
import ua.ck.ghplayer.utils.ImageUtils;

public class CustomTrackList {
    private Context context;
    private static ArrayList<Track> customTrackList = new ArrayList<>();

    public CustomTrackList(Context context) {
        this.context = context;
    }

    public void setCustomTrackList(Cursor cursor){
        customTrackList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(Track.ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Track.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(Track.ARTIST));
                String album = cursor.getString(cursor.getColumnIndexOrThrow(Track.ALBUM));
                long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(Track.ALBUM_ID));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(Track.DURATION));
                Uri albumArt = ImageUtils.getAlbumArt(context, albumId);

                customTrackList.add(new Track(id, title, artist, album, albumId, albumArt, duration));
            } while (cursor.moveToNext());
        }
    }

    public ArrayList<Track> getCustomTrackList() {
        return customTrackList;
    }

}
