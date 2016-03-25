package ua.ck.ghplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.GenreListAdapter;
import ua.ck.ghplayer.events.ShowTrackListFragment;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.GenreList;
import ua.ck.ghplayer.loaders.GenreListLoader;
import ua.ck.ghplayer.utils.Constants;

public class GenreListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {

    private static final int ID_GENRE_LIST_LOADER = 4;
    private RecyclerView genreListRecyclerView;
    private GenreListAdapter genreListAdapter;

    // Instance's
    private EventBus eventBus = EventBus.getDefault();

    public GenreListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genreListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        genreListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        genreListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        genreListRecyclerView.setHasFixedSize(true);

        // RecyclerView - Add TouchListener
        RecyclerViewTouchListener genreListTouchListener = new RecyclerViewTouchListener(getContext(), this, genreListRecyclerView);
        genreListRecyclerView.addOnItemTouchListener(genreListTouchListener);

        // RecyclerView - Set Adapter
        genreListAdapter = new GenreListAdapter();
        genreListRecyclerView.setAdapter(genreListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // GenreList Loader
        getLoaderManager().initLoader(ID_GENRE_LIST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new GenreListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            GenreList.getInstance().setGenreList(getContext(), data);
            genreListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view, int position) {
        eventBus.post(new ShowTrackListFragment(Constants.SHOW_GENRE_TRACK_LIST, GenreList.getInstance().getGenreList().get(position).getId()));
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}
