package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Artist;
import ua.ck.ghplayer.models.ArtistInfo;
import ua.ck.ghplayer.utils.ArtistInfoUtils;

public class ArtistList {
    private static ArtistList ourInstance = new ArtistList();
    private static ArrayList<Artist> artistList = new ArrayList<>();

    private ArtistList() {
    }

    public static ArtistList getInstance() {
        return ourInstance;
    }

    public ArrayList<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(Context context, Cursor cursor) {
        clearArtistList();
        ArtistInfo artistInfo;
        String artistArtUrl = null;

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                artistInfo = ArtistInfoUtils.getArtistInfo(context,
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));

                artistArtUrl = null;
                if (artistInfo != null) {
                    artistArtUrl = artistInfo.getArtistArtUrl();
                }
                artistList.add(new Artist(cursor, artistArtUrl));
            } while (cursor.moveToNext());
        }
    }

    public void clearArtistList() {
        artistList.clear();
    }

}
