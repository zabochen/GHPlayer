package ua.ck.ghplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.ArtistInfoViewPagerAdapter;
import ua.ck.ghplayer.events.ShowTrackListActivity;
import ua.ck.ghplayer.fragments.AlbumListFragment;
import ua.ck.ghplayer.fragments.ArtistAboutFragment;
import ua.ck.ghplayer.fragments.ArtistTrackListFragment;
import ua.ck.ghplayer.models.ArtistInfo;
import ua.ck.ghplayer.utils.ArtistInfoUtils;
import ua.ck.ghplayer.utils.Constants;

public class ArtistInfoActivity extends AppCompatActivity {
    private String artistName;
    private long artistId;
    private ArtistInfo artistInfo;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private EventBus eventBus = EventBus.getDefault();


    private int trackListId;
    private int trackPosition;
    private boolean miniPlayerGone;
    private boolean buttonPauseVisible;


    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_info);

        getExtraBundle();
        setToolbar();
        setView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExtraBundle() {
        Bundle intentBundle = getIntent().getExtras();
        artistName = intentBundle.getString("ARTIST_NAME");
        artistId = intentBundle.getLong("ARTIST_ID");
        ArtistInfoUtils.setArtistInfo(getApplicationContext(), artistName);
        artistInfo = ArtistInfoUtils.getArtistInfo(getApplicationContext(), artistName);

        miniPlayerGone = intentBundle.getBoolean(Constants.MINI_PLAYER_GONE_KEY);
        trackListId = intentBundle.getInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY);
        trackPosition = intentBundle.getInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY);
        buttonPauseVisible = intentBundle.getBoolean(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY);
    }

    private Bundle setBundle() {
        Bundle intentBundle = new Bundle();
        intentBundle.putString("ARTIST_NAME", artistName);
        intentBundle.putLong("ARTIST_ID", artistId);

        intentBundle.putBoolean(Constants.MINI_PLAYER_GONE_KEY, miniPlayerGone);
        intentBundle.putInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY, trackListId);
        intentBundle.putInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
        intentBundle.putBoolean(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY, buttonPauseVisible);
        return intentBundle;
    }


    private void setToolbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.activity_artist_info__toolbar);
        setSupportActionBar(toolbar);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(artistName);
        }
    }

    private void setView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ArtistInfoViewPagerAdapter adapter = new ArtistInfoViewPagerAdapter(getSupportFragmentManager());

        AlbumListFragment albumListFragment = new AlbumListFragment();
        albumListFragment.setArguments(setBundle());

        ArtistTrackListFragment artistTrackListFragment = new ArtistTrackListFragment();
        artistTrackListFragment.setArguments(setBundle());


        adapter.addFragment(artistTrackListFragment, "Tracks");
        adapter.addFragment(albumListFragment, "Albums");
        adapter.addFragment(new ArtistAboutFragment(), "About");
        viewPager.setAdapter(adapter);
    }

    @Subscribe
    public void onShowTrackListActivity(ShowTrackListActivity event) {

        switch (event.getTrackListId()) {
            case (Constants.ALBUM_TRACK_LIST_ID):
                Intent albumTrackListActivityIntent = new Intent(getApplicationContext(), ArtistTrackListActivity.class);
                // MiniPlayer current state (Gone/Visible)
                albumTrackListActivityIntent.putExtra(Constants.MINI_PLAYER_GONE_KEY, miniPlayerGone);
                // Selected Album Position
                albumTrackListActivityIntent.putExtra(Constants.ACTIVITY_RESULT_ITEM_POSITION_INTENT_KEY, event.getPosition());
                // If MiniPlayer Visible
                if (!miniPlayerGone) {
                    // Current TrackList ID
                    albumTrackListActivityIntent.putExtra(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY, trackListId);
                    // Current Track position
                    albumTrackListActivityIntent.putExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
                    // Button Pause Status
                    albumTrackListActivityIntent.putExtra(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY, buttonPauseVisible);
                }
                // requestCode = TrackList ID
                startActivityForResult(albumTrackListActivityIntent, event.getTrackListId());


                break;
        }
    }


}
