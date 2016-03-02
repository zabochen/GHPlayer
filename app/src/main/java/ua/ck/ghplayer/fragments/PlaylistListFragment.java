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
import ua.ck.ghplayer.adapters.PlaylistListAdapter;
import ua.ck.ghplayer.lists.PlaylistList;
import ua.ck.ghplayer.models.Playlist;

public class PlaylistListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_PLAYLIST_LIST_LOADER = 5;
    RecyclerView playlistListRecyclerView;
    PlaylistListAdapter playlistListAdapter;


    public PlaylistListFragment() {
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

        playlistListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        playlistListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //RecyclerViewTouchListener playlistListTouchListener = new RecyclerViewTouchListener(getContext(), this, playlistListRecyclerView);
        //playlistListRecyclerView.addOnItemTouchListener(playlistListTouchListener);
        playlistListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_PLAYLIST_LIST_LOADER, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), Playlist.EXTERNAL_CONTENT_URI, Playlist.projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            PlaylistList playlistList = PlaylistList.getInstance();
            playlistList.setPlaylistList(data);
            playlistListAdapter = new PlaylistListAdapter(playlistList.getPlaylistList());

            playlistListRecyclerView.setAdapter(playlistListAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
