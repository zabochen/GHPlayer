package ua.ck.ghplayer.fragments;


import android.content.Intent;
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

import org.greenrobot.eventbus.EventBus;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.activities.ArtistInfoActivity;
import ua.ck.ghplayer.adapters.ArtistListAdapter;
import ua.ck.ghplayer.events.ShowTrackListActivity;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.ArtistList;
import ua.ck.ghplayer.models.Artist;
import ua.ck.ghplayer.utils.Constants;

public class ArtistListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {
    private static final int ID_ARTIST_LIST_LOADER = 3;
    RecyclerView artistListRecyclerView;
    ArtistListAdapter artistListAdapter;


    public ArtistListFragment() {
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

        artistListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        artistListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        artistListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        artistListRecyclerView.setHasFixedSize(true);

        RecyclerViewTouchListener artistListTouchListener = new RecyclerViewTouchListener(getContext(), this, artistListRecyclerView);
        artistListRecyclerView.addOnItemTouchListener(artistListTouchListener);

        artistListAdapter = new ArtistListAdapter();
        artistListRecyclerView.setAdapter(artistListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_ARTIST_LIST_LOADER, savedInstanceState, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), Artist.EXTERNAL_CONTENT_URI, Artist.projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            ArtistList artistList = ArtistList.getInstance();
            artistList.setArtistList(getActivity(), data);
            artistListAdapter.setData(getContext(), artistList.getArtistList(),getActivity());

            artistListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(View view, int position) {
        EventBus.getDefault().post(new ShowTrackListActivity(Constants.ARTIST_ALBUM_TRACK_LIST_ID, position));
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
