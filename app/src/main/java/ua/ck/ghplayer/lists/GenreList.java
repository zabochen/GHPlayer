package ua.ck.ghplayer.lists;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Genre;

public class GenreList {

    private static GenreList instance;
    private static ArrayList<Genre> genreList;

    private GenreList() {
    }

    public static GenreList getInstance() {
        if (instance == null) {
            instance = new GenreList();
        }
        return instance;
    }

    public void setGenreList(Context context, Cursor cursor) {
        cursor.moveToFirst();
        genreList = new ArrayList<>();

        do {
            // Get The Value Of Fields
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(Genre.ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Genre.NAME));

            // Add Object To ArrayList
            Genre genre = new Genre(id, name);
            genreList.add(genre);
        } while (cursor.moveToNext());

    }

    public ArrayList<Genre> getGenreList() {
        return genreList != null ? genreList : null;
    }

}
