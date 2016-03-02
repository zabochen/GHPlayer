package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Artist;

public class ArtistList {
    private static ArtistList ourInstance = new ArtistList();
    private static ArrayList<Artist> artistList = new ArrayList();

    private ArtistList() {
    }

    public static ArtistList getInstance() {
        return ourInstance;
    }

    public ArrayList<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(Cursor cursor) {
        clearArtistList();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                artistList.add(new Artist(cursor));
            } while (cursor.moveToNext());
        }
    }

    public void clearArtistList() {
        artistList.clear();
    }

}
