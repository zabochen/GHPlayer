package ua.ck.ghplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.GenreListAdapter;
import ua.ck.ghplayer.lists.GenreList;
import ua.ck.ghplayer.models.Genre;

public class GenreListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_GENRE_LIST_LOADER = 4;
    RecyclerView genreListRecyclerView;
    GenreListAdapter genreListAdapter;


    public GenreListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genreListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        genreListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //RecyclerViewTouchListener genreListTouchListener = new RecyclerViewTouchListener(getContext(), this, genreListRecyclerView);
        //genreListRecyclerView.addOnItemTouchListener(genreListTouchListener);
        genreListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_GENRE_LIST_LOADER, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), Genre.EXTERNAL_CONTENT_URI, Genre.projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            GenreList genreList = GenreList.getInstance();
            genreList.setGenreList(data);
            genreListAdapter = new GenreListAdapter(genreList.getGenreList());

            genreListRecyclerView.setAdapter(genreListAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
