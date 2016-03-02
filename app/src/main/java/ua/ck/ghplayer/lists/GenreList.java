package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Genre;

public class GenreList {
    private static GenreList ourInstance = new GenreList();
    private static ArrayList<Genre> genreList = new ArrayList();

    private GenreList() {
    }

    public static GenreList getInstance() {
        return ourInstance;
    }

    public ArrayList<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(Cursor cursor) {
        clearGenreList();
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                genreList.add(new Genre(cursor));
            } while (cursor.moveToNext());
        }
    }

    public void clearGenreList() {
        genreList.clear();
    }
}
