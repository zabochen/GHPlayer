package ua.ck.ghplayer.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.lists.CustomTrackList;
import ua.ck.ghplayer.loaders.CustomTrackListLoader;
import ua.ck.ghplayer.models.Track;

public class CustomTrackListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {
    private static final int ID_CUSTOM_TRACK_LIST_LOADER = 77;
    private String CHOICE_MODE;
    private String name;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_track_list);
        getExtraBundle();


        getLoaderManager().initLoader(ID_CUSTOM_TRACK_LIST_LOADER, null, this);
    }

    private void getExtraBundle() {
        Bundle intentBundle = getIntent().getExtras();

        CHOICE_MODE = intentBundle.getString("CHOICE_MODE");

        if (CHOICE_MODE == null) {
            finish();
        }

        switch (CHOICE_MODE) {
            case "ALBUM":
                name = intentBundle.getString("ALBUM_NAME");
                id = intentBundle.getLong("ALBUM_ID");
                break;
            case "ARTIST":
                name = intentBundle.getString("ARTIST_NAME");
                id = intentBundle.getLong("ARTIST_ID");
                break;
            case "PLAYLIST":
                name = intentBundle.getString("PLAYLIST_NAME");
                id = intentBundle.getLong("PLAYLIST_ID");
                break;
            default:
                finish();
                break;
        }


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CustomTrackListLoader(this, getIntent().getExtras().getString("CHOICE_MODE"), this.id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            CustomTrackList customTrackList = new CustomTrackList(this);
            customTrackList.setCustomTrackList(data);
/*
            customTrackListAdapter.setData(this,
                    customTrackList).getCustomTrackList();

            customTrackListRecyclerView.getAdapter().notifyDataSetChanged();
*/
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }
}


