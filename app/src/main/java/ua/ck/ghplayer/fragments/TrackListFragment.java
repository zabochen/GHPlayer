package ua.ck.ghplayer.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alertdialogpro.AlertDialogPro;
import com.daimajia.swipe.util.Attributes;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.TrackListAdapter;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.loaders.TrackListLoader;
import ua.ck.ghplayer.utils.Constants;

public class TrackListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView trackListRecyclerView;
    private TrackListAdapter trackListAdapter;

    // Preferences
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    // Dialog Choice Item
    int choiceItem = Constants.SORT_TRACK_LIST_TITLE;

    public TrackListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add Own Menu Item's
        setHasOptionsMenu(true);
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
        trackListAdapter = new TrackListAdapter();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_track_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menu_track_list_fragment_sorting):
                dialogPro();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogPro() {

        sharedPreferences = getContext()
                .getSharedPreferences(Constants.PREFERENCES_FILE_NAME, Constants.PREFERENCES_FILE_MODE);
        sharedPreferencesEditor = sharedPreferences.edit();

        AlertDialogPro.Builder alertDialogBuilder = new AlertDialogPro.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.menu_fragments_sorting_title);

        int preferencesItem = Constants.SORT_TRACK_LIST_TITLE;

        switch (sharedPreferences.getInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_TITLE)) {
            case (Constants.SORT_TRACK_LIST_ALBUM):
            case (Constants.SORT_TRACK_LIST_ALBUM_REVERSE):
                preferencesItem = Constants.SORT_TRACK_LIST_ALBUM;
                break;
            case (Constants.SORT_TRACK_LIST_ARTIST):
            case (Constants.SORT_TRACK_LIST_ARTIST_REVERSE):
                preferencesItem = Constants.SORT_TRACK_LIST_ARTIST;
                break;
            case (Constants.SORT_TRACK_LIST_TITLE):
            case (Constants.SORT_TRACK_LIST_TITLE_REVERSE):
                preferencesItem = Constants.SORT_TRACK_LIST_TITLE;
                break;
        }

        alertDialogBuilder.setSingleChoiceItems(
                R.array.menu_fragment_track_list_sorting_dialog,
                preferencesItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choiceItem = which;
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.menu_fragments_sorting_dialog_button_descending,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (choiceItem == 0) {
                            // Album Sorting
                            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_ALBUM_REVERSE);
                            TrackList.getInstance().sortAlbumReverse();
                        } else if (choiceItem == 1) {
                            // Artist Sorting
                            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_ARTIST_REVERSE);
                            TrackList.getInstance().sortArtistReverse();
                        } else if (choiceItem == 2) {
                            // Title Sorting
                            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_TITLE_REVERSE);
                            TrackList.getInstance().sortTitleReverse();
                        }

                        sharedPreferencesEditor.apply();
                        trackListAdapter.notifyDataSetChanged();
                    }
                });

        alertDialogBuilder.setPositiveButton(R.string.menu_fragments_sorting_dialog_button_ascending,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (choiceItem == 0) {
                            // Album Sorting
                            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_ALBUM);
                            TrackList.getInstance().sortAlbum();
                        } else if (choiceItem == 1) {
                            // Artist Sorting
                            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_ARTIST);
                            TrackList.getInstance().sortArtist();
                        } else if (choiceItem == 2) {
                            // Title Sorting
                            sharedPreferencesEditor.putInt(Constants.PREFERENCES_KEY_SORT_TRACK_LIST, Constants.SORT_TRACK_LIST_TITLE);
                            TrackList.getInstance().sortTitle();
                        }

                        sharedPreferencesEditor.apply();
                        trackListAdapter.notifyDataSetChanged();
                    }
                });

        alertDialogBuilder.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TrackList Loader
        getLoaderManager().initLoader(Constants.MAIN_TRACK_LIST_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new TrackListLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            TrackList.getInstance().setTrackList(getContext(), data);
            trackListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
