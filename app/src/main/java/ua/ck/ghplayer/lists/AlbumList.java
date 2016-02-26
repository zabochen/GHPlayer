package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Album;

public class AlbumList {
    private static AlbumList ourInstance = new AlbumList();
    private static ArrayList albumList = new ArrayList();

    private AlbumList() {
    }

    public static AlbumList getInstance() {
        return ourInstance;
    }

    public ArrayList getAlbumList() {
        return albumList;
    }

    public void setAlbumList(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do{
                albumList.add(new Album(cursor));
            }while(cursor.moveToNext());
        }
    }

    public void clearAlbumList(){
        albumList.clear();
    }


}
