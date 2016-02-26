package ua.ck.ghplayer.lists;

import android.database.Cursor;

import java.util.ArrayList;

import ua.ck.ghplayer.models.Genre;

public class GenreList {
    private static GenreList ourInstance = new GenreList();
    private static ArrayList genreList = new ArrayList();

    public static GenreList getInstance() {
        return ourInstance;
    }

    private GenreList() {
    }

    public void setGenreList(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            do{
                genreList.add(new Genre(cursor));
            }while(cursor.moveToNext());
        }
    }

    public ArrayList getGenreList() {
        return genreList;
    }

    public void clearGenreList(){
        genreList.clear();
    }
}
