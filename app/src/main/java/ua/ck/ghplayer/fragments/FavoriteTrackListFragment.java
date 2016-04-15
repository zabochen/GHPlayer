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

import com.daimajia.swipe.util.Attributes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.FavoriteTrackListAdapter;
import ua.ck.ghplayer.events.UpdateFavoritesEvent;
import ua.ck.ghplayer.lists.FavoriteTrackList;
import ua.ck.ghplayer.loaders.FavoriteTrackListLoader;
import ua.ck.ghplayer.utils.Constants;

public class FavoriteTrackListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView trackListRecyclerView;
    private FavoriteTrackListAdapter trackListAdapter;

    private EventBus eventBus = EventBus.getDefault();

    public FavoriteTrackListFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
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
        trackListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        trackListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trackListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trackListRecyclerView.setHasFixedSize(true);

        // RecyclerView - Set Adapter
        trackListAdapter = new FavoriteTrackListAdapter();
        trackListAdapter.setMode(Attributes.Mode.Single);
        trackListRecyclerView.setAdapter(trackListAdapter);

        // RecyclerView - Set Scroll Listener
        trackListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                trackListAdapter.mItemManger.closeAllItems();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Favorites TrackList Loader
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
            trackListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Subscribe
    public void onUpdateFavoritesEvent(UpdateFavoritesEvent event) {
        getLoaderManager().restartLoader(Constants.FAVORITE_TRACK_LIST_LOADER_ID, null, this);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }
}
