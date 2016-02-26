package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Artist;

public class ArtistList {
    private static ArtistList ourInstance = new ArtistList();
    private static ArrayList artistList = new ArrayList();

    public static ArtistList getInstance() {
        return ourInstance;
    }

    private ArtistList() {
    }

    public void setArtistList(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do{
                artistList.add(new Artist(cursor));
            }while(cursor.moveToNext());
        }
    }

    public ArrayList getArtistList() {
        return artistList;
    }

    public void clearArtistList(){
        artistList.clear();
    }

}
