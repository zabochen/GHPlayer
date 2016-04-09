package ua.ck.ghplayer.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.AlbumListAdapter;
import ua.ck.ghplayer.lists.AlbumList;
import ua.ck.ghplayer.lists.CustomAlbumList;
import ua.ck.ghplayer.models.Album;

public class AlbumListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_ALBUM_LIST_LOADER = 2;
    private RecyclerView albumListRecyclerView;
    private AlbumListAdapter albumListAdapter;
    private boolean flagCustom = false;
    private ArrayList<Album> dataArray;

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
        albumListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        albumListRecyclerView.setHasFixedSize(true);

        albumListAdapter = new AlbumListAdapter();
        albumListRecyclerView.setAdapter(albumListAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_ALBUM_LIST_LOADER, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey("ARTIST_ID")) {
            String[] selectionArgs = new String[]{String.valueOf(bundle.getLong("ARTIST_ID"))};
            flagCustom = true;

            return new CursorLoader(getContext(),
                    MediaStore.Audio.Artists.Albums.getContentUri("external", bundle.getLong("ARTIST_ID")),
                    Album.projection,
                    Album.selection,
                    selectionArgs,
                    null);
        } else {
            return new CursorLoader(getContext(), Album.EXTERNAL_CONTENT_URI, Album.projection, null, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            if (flagCustom) {
                CustomAlbumList albumList = CustomAlbumList.getInstance();
                albumList.setAlbumList(data);
                dataArray = albumList.getAlbumList();
            } else {
                AlbumList albumList = AlbumList.getInstance();
                albumList.setAlbumList(data);
                dataArray = albumList.getAlbumList();
            }

            albumListAdapter.setData(getContext(), dataArray);
            albumListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
