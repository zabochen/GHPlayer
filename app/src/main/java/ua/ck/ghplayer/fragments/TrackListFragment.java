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
import android.widget.LinearLayout;
import android.widget.Toast;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.TrackListAdapter;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.loaders.TrackListLoader;

public class TrackListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {

    private static final int ID_TRACK_LIST_LOADER = 1;
    private RecyclerView trackListRecyclerView;

    public TrackListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_track_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.trackListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_track_list_recycler_view);
        trackListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewTouchListener trackListTouchListener = new RecyclerViewTouchListener(getContext(), this, trackListRecyclerView);
        trackListRecyclerView.addOnItemTouchListener(trackListTouchListener);
        trackListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TrackList Loader
        getLoaderManager().initLoader(ID_TRACK_LIST_LOADER, null, this);
    }

    private void setTrackListAdapter() {
        TrackListAdapter trackListAdapter = new TrackListAdapter();
        trackListRecyclerView.setAdapter(trackListAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TrackListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            TrackList.getInstance().setTrackList(data);
            setTrackListAdapter();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
    }
}
