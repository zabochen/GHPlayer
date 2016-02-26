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
import android.widget.Toast;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.AlbumListAdapter;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.AlbumList;
import ua.ck.ghplayer.models.Album;

public class AlbumListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>/*,ItemClickListener*/ {
    private static final int ID_ALBUM_LIST_LOADER = 2;
    RecyclerView albumListRecyclerView;
    AlbumListAdapter albumListAdapter;


    public AlbumListFragment() {
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

        albumListRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recycler_view);
        albumListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //RecyclerViewTouchListener albumListTouchListener = new RecyclerViewTouchListener(getContext(), this, albumListRecyclerView);
        //albumListRecyclerView.addOnItemTouchListener(albumListTouchListener);
        albumListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_ALBUM_LIST_LOADER, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), Album.EXTERNAL_CONTENT_URI, Album.projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            AlbumList albumList = AlbumList.getInstance();
            albumList.setAlbumList(data);
            albumListAdapter = new AlbumListAdapter(albumList.getAlbumList());

            albumListRecyclerView.setAdapter(albumListAdapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
