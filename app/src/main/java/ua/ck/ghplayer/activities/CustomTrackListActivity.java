package ua.ck.ghplayer.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.CustomTrackListAdapter;
import ua.ck.ghplayer.lists.CustomTrackList;
import ua.ck.ghplayer.loaders.CustomTrackListLoader;

public class CustomTrackListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_CUSTOM_TRACK_LIST_LOADER = 77;
    private String CHOICE_MODE;
    private String name;
    private long id;

    private Toolbar toolbar;
    private RecyclerView customTrackListRecyclerView;

    private CustomTrackListAdapter customTrackListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
        getExtraBundle();
        getLoaderManager().initLoader(ID_CUSTOM_TRACK_LIST_LOADER, null, this);
        setToolbar();
        setView();
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

    // Find View's & Set Listener
    private void setView() {
        // RecyclerView - Base Configuration
        customTrackListRecyclerView = (RecyclerView) findViewById(R.id.activity_track_list_recycler_view);
        customTrackListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        customTrackListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        customTrackListRecyclerView.setHasFixedSize(true);

        // RecyclerView - Set Adapter
        customTrackListAdapter = new CustomTrackListAdapter();
        customTrackListRecyclerView.setAdapter(customTrackListAdapter);
    }

    // Set Toolbar
    private void setToolbar() {
        // Toolbar
        this.toolbar = (Toolbar) findViewById(R.id.activity_track_list_toolbar);
        setSupportActionBar(toolbar);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setTitle();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Home Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Loader GenreTrackList
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CustomTrackListLoader(this, getIntent().getExtras().getString("CHOICE_MODE"), this.id, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            CustomTrackList customTrackList = new CustomTrackList(this);
            customTrackList.setCustomTrackList(data);

            customTrackListAdapter.setData(this, customTrackList.getCustomTrackList());
            customTrackListRecyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}


