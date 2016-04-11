package ua.ck.ghplayer.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.FavoriteTrackListAdapter;
import ua.ck.ghplayer.events.StartMiniPlayerEvent;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchHelper;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.FavoriteTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.loaders.FavoriteTrackListLoader;
import ua.ck.ghplayer.utils.Constants;

public class FavoriteListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {

    private RecyclerView favoriteListRecyclerView;
    private FavoriteTrackListAdapter favoriteListAdapter;

    // Instances
    EventBus eventBus = EventBus.getDefault();

    public FavoriteListFragment() {
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RecyclerView - Base Configuration
        favoriteListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        favoriteListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favoriteListRecyclerView.setHasFixedSize(true);

        // RecyclerView - Add TouchListener
        RecyclerViewTouchListener favoriteListTouchListener = new RecyclerViewTouchListener(getContext(), this, favoriteListRecyclerView);
        favoriteListRecyclerView.addOnItemTouchListener(favoriteListTouchListener);

        // RecyclerView - Set Adapter
        favoriteListAdapter = new FavoriteTrackListAdapter();
        favoriteListRecyclerView.setAdapter(favoriteListAdapter);

        // RecyclerView - Add Playlist
        //ItemTouchHelper.Callback favoriyeListItemCallback = new RecyclerViewTouchHelper(favoriteListAdapter);
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(favoriyeListItemCallback);
        //itemTouchHelper.attachToRecyclerView(favoriteListRecyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // FavoriteList Loader
        getLoaderManager().initLoader(Constants.FAVORITE_TRACK_LIST_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new FavoriteTrackListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            FavoriteTrackList.getInstance().setFavoriteTrackList(getContext(), data);
            favoriteListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onClick(View view, int position) {
        // Sending the current track position in the MiniPlayer
        eventBus.post(new StartMiniPlayerEvent(Constants.MAIN_TRACK_LIST_ID, position));
    }

    @Override
    public void onLongClick(View view, int position) {

    }

}
