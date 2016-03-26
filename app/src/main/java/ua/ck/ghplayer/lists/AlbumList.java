package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Album;

public class AlbumList {
    private static AlbumList ourInstance = new AlbumList();
    private static ArrayList<Album> albumList = new ArrayList();

    private AlbumList() {
    }

    public static AlbumList getInstance() {
        return ourInstance;
    }

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(Cursor cursor) {
        clearAlbumList();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                albumList.add(new Album(cursor));
            } while (cursor.moveToNext());
        }
    }

    public void clearAlbumList() {
        albumList.clear();
    }


}
