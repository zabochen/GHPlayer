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
import ua.ck.ghplayer.adapters.GenreTrackListAdapter;
import ua.ck.ghplayer.events.StartMiniPlayerEvent;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.loaders.GenreTrackListLoader;
import ua.ck.ghplayer.utils.Constants;

public class GenreTrackListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {

    private static final int ID_TRACK_LIST_LOADER = 100;
    private RecyclerView trackListRecyclerView;
    private GenreTrackListAdapter genreTrackListAdapter;

    // Genre ID
    private long genreId;

    // Instances
    EventBus eventBus = EventBus.getDefault();

    public GenreTrackListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        genreId = getArguments().getLong(Constants.SHOW_GENRE_TRACK_LIST_BUNDLE_KEY);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView - Base Configuration
        trackListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        trackListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trackListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trackListRecyclerView.setHasFixedSize(true);

        // RecyclerView - Add TouchListener
        RecyclerViewTouchListener trackListTouchListener = new RecyclerViewTouchListener(getContext(), this, trackListRecyclerView);
        trackListRecyclerView.addOnItemTouchListener(trackListTouchListener);

        // RecyclerView - Set Adapter
        genreTrackListAdapter = new GenreTrackListAdapter();
        trackListRecyclerView.setAdapter(genreTrackListAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TrackList Loader
        getLoaderManager().initLoader(ID_TRACK_LIST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        //  Before entering the genre, clear GenreTrackLst
        if (GenreTrackList.getInstance().getGenreTrackList() != null) {
            GenreTrackList.getInstance().getGenreTrackList().clear();
        }

        return new GenreTrackListLoader(getContext(), genreId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Set GenreTrackList
        if (data != null && data.moveToFirst()) {
            GenreTrackList.getInstance().setGenreTrackList(getContext(), data);
            trackListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view, int position) {
        // Sending the current track position in the MiniPlayer
        eventBus.post(new StartMiniPlayerEvent(Constants.GENRE_TRACK_LIST_ID, position));
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}
