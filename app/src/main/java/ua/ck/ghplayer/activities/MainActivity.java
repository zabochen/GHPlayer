package ua.ck.ghplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.events.ShowTrackListFragment;
import ua.ck.ghplayer.events.StartMiniPlayerEvent;
import ua.ck.ghplayer.events.StopMiniPlayerEvent;
import ua.ck.ghplayer.fragments.AlbumListFragment;
import ua.ck.ghplayer.fragments.ArtistListFragment;
import ua.ck.ghplayer.fragments.GenreListFragment;
import ua.ck.ghplayer.fragments.GenreTrackListFragment;
import ua.ck.ghplayer.fragments.PlaylistListFragment;
import ua.ck.ghplayer.fragments.TrackListFragment;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.services.MusicService;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.ServiceUtils;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    // Values
    private static int navigationViewItemSelected;
    private boolean miniPlayerGone;
    private int trackListId;
    private int trackPosition;

    // Views
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RelativeLayout miniPlayer;

    // MiniPlayer View's
    private ImageView imageViewAlbumArt;
    private TextView title;
    private Button buttonPrevious;
    private Button buttonPause;
    private Button buttonStop;

    // Fragments
    private FragmentManager fragmentManager;

    // Instances
    private EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventBus.register(this);

        setContentView(R.layout.activity_main);
        setView();
        setInstanceState(savedInstanceState);
        setToolbar();
        setNavigationView();
        setMiniPlayerGone(miniPlayerGone);
        setFragment();
    }

    private void setView() {
        // Find View
        imageViewAlbumArt = (ImageView) findViewById(R.id.activity_mini_player_album_art);
        buttonStop = (Button) findViewById(R.id.activity_mini_player_button_stop);
        title = (TextView) findViewById(R.id.activity_mini_player_title);

        // Set Listener
        imageViewAlbumArt.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.MINI_PLAYER_GONE_KEY, miniPlayerGone);
        outState.putInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY, trackListId);
        outState.putInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
    }

    private void setInstanceState(Bundle bundle) {
        if (bundle == null) {
            // Mini Player Gone
            miniPlayerGone = true;

            // Navigation View
            navigationViewItemSelected = R.id.menu_navigation_view_item_tracks;
        } else {
            // MiniPlayer
            miniPlayerGone = bundle.getBoolean(Constants.MINI_PLAYER_GONE_KEY);
            trackListId = bundle.getInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY);
            trackPosition = bundle.getInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY);
            setMiniPlayerTrackTitle(trackListId, trackPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Close Navigation View
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setToolbar() {
        // Toolbar
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    private void setNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.toggle_navigation_view_open, R.string.toggle_navigation_view_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_navigation_view);
        //navigationView.inflateHeaderView(R.layout.navigation_view_header);
        navigationView.inflateMenu(R.menu.menu_navigation_view);
        //navigationView.getMenu().findItem(navigationViewItemSelected).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment() {
        // Activity Container & Fragment
        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        Fragment listFragment = null;
        Fragment optionalFragment = null;
        //todo: add function for Boolean isLandscapeOrientedTablet
        boolean isLandscapeOrientedTablet = false;

        switch (navigationViewItemSelected) {
            case R.id.menu_navigation_view_item_tracks:
                TrackListFragment trackListFragment = new TrackListFragment();
                listFragment = trackListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_albums:
                AlbumListFragment albumListFragment = new AlbumListFragment();
                listFragment = albumListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_artists:
                ArtistListFragment artistListFragment = new ArtistListFragment();
                listFragment = artistListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_genres:
                GenreListFragment genreListFragment = new GenreListFragment();
                listFragment = genreListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            case R.id.menu_navigation_view_item_playlists:
                PlaylistListFragment playlistListFragment = new PlaylistListFragment();
                listFragment = playlistListFragment;
                if (isLandscapeOrientedTablet) {

                }
                break;
            default:
                break;
        }

        // Add Fragments
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(activityFrameLayout.getId(), listFragment);

        if (isLandscapeOrientedTablet) {
            //fragmentTransaction.replace(, optionalFragment);
        }

        fragmentTransaction.commit();
    }

    @Subscribe
    public void onShowTrackListFragment(ShowTrackListFragment event) {

        switch (event.getShowTrackList()) {
            case (Constants.SHOW_GENRE_TRACK_LIST):
                // Send Genre ID to Fragment
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.SHOW_GENRE_TRACK_LIST_BUNDLE_KEY, event.getId());

                FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);

                GenreTrackListFragment genreTrackListFragment = new GenreTrackListFragment();
                genreTrackListFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(activityFrameLayout.getId(), genreTrackListFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        navigationViewItemSelected = item.getItemId();
        setFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe
    public void onStartMiniPlayerEvent(StartMiniPlayerEvent event) {

        this.trackListId = event.getTrackListId();
        this.trackPosition = event.getPosition();

        // Visible MiniPlayer
        if (miniPlayer.getVisibility() == View.GONE) {
            setMiniPlayerGone(false);
        }

        // Start MusicService
        Intent musicServiceStartIntent = new Intent(getApplicationContext(), MusicService.class);
        musicServiceStartIntent.putExtra(Constants.TRACK_LISTS_ID_KEY, trackListId);
        musicServiceStartIntent.putExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
        startService(musicServiceStartIntent);

        // Set MiniPlayer track title
        setMiniPlayerTrackTitle(trackListId, trackPosition);
    }

    private void setMiniPlayerTrackTitle(int trackListId, int trackPosition) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (trackListId) {
            case (Constants.MAIN_TRACK_LIST_ID):
                stringBuilder.append(TrackList.getInstance().getTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(TrackList.getInstance().getTrackList().get(trackPosition).getTitle());
                break;
            case (Constants.ALBUM_TRACK_LIST_ID):
                break;
            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                break;
            case (Constants.GENRE_TRACK_LIST_ID):
                stringBuilder.append(GenreTrackList.getInstance().getGenreTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(GenreTrackList.getInstance().getGenreTrackList().get(trackPosition).getTitle());
                break;
        }

        title.setText(stringBuilder.toString());
        title.setSelected(true);

    }

    private void setMiniPlayerGone(boolean miniPlayerGone) {
        miniPlayer = (RelativeLayout) findViewById(R.id.activity_mini_player_container);
        if (miniPlayerGone) {
            Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            miniPlayer.startAnimation(bottomUp);
            miniPlayer.setVisibility(View.GONE);
            this.miniPlayerGone = true;
        } else {
            Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            miniPlayer.startAnimation(bottomUp);
            miniPlayer.setVisibility(View.VISIBLE);
            this.miniPlayerGone = false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.activity_mini_player_album_art):
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class));
                break;
//            case (R.id.activity_mini_player_button_play):
//                eventBus.post(new PlayMiniPlayerEvent(miniPlayerTrackPosition));
//                break;
            case (R.id.activity_mini_player_button_stop):
                eventBus.post(new StopMiniPlayerEvent());
                setMiniPlayerGone(true);
                Intent musicServiceStopIntent = new Intent(getApplicationContext(), MusicService.class);
                stopService(musicServiceStopIntent);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Start Notification Player
        if (ServiceUtils.musicServiceRunning(getApplicationContext(), MusicService.class)) {
            eventBus.post(new NotificationPlayerEvent(Constants.NOTIFICATION_PLAYER_START));
        }
    }

    @Override
    protected void onDestroy() {
        // Event Bus
        eventBus.unregister(this);
        super.onDestroy();
    }
}
