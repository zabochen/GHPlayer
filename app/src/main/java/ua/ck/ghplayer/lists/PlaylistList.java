package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Playlist;

public class PlaylistList {
    private static PlaylistList ourInstance = new PlaylistList();
    private static ArrayList playlistList = new ArrayList();

    public static PlaylistList getInstance() {
        return ourInstance;
    }

    private PlaylistList() {
    }

    public void setPlaylistList(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do{
                playlistList.add(new Playlist(cursor));
            }while(cursor.moveToNext());
        }
    }

    public ArrayList getPlaylistList() {
        return playlistList;
    }

    public void clearPlaylistList(){
        playlistList.clear();
    }
}
