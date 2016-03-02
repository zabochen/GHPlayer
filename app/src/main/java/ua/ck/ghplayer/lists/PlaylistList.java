package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Playlist;

public class PlaylistList {
    private static PlaylistList ourInstance = new PlaylistList();
    private static ArrayList<Playlist> playlistList = new ArrayList();

    private PlaylistList() {
    }

    public static PlaylistList getInstance() {
        return ourInstance;
    }

    public ArrayList<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(Cursor cursor) {
        clearPlaylistList();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                playlistList.add(new Playlist(cursor));
            } while (cursor.moveToNext());
        }
    }

    public void clearPlaylistList() {
        playlistList.clear();
    }
}
