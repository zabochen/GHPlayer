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
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ua.ck.ghplayer.R;
import ua.ck.ghplayer.events.StartMiniPlayerEvent;
import ua.ck.ghplayer.events.StopMiniPlayerEvent;
import ua.ck.ghplayer.fragments.AlbumListFragment;
import ua.ck.ghplayer.fragments.ArtistListFragment;
import ua.ck.ghplayer.fragments.GenreListFragment;
import ua.ck.ghplayer.fragments.PlaylistListFragment;
import ua.ck.ghplayer.fragments.TrackListFragment;
import ua.ck.ghplayer.interfaces.ItemClickFragmentSetter;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.services.MusicService;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ItemClickFragmentSetter,
        View.OnClickListener {


    // Bundle
    private static final String KEY_NAVIGATION_VIEW_ITEM_SELECTED = "NavigationViewItemSelected";
    private static final String KEY_MINI_PLAYER_GONE = "MiniPlayerGone";
    private static final String KEY_MINI_PLAYER_TRACK_POSITION = "MiniPlayerTrackPosition";

    // Values
    private static int navigationViewItemSelected;
    private static boolean miniPlayerGone;
    private static int miniPlayerTrackPosition;

    // Views
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RelativeLayout miniPlayer;

    // MiniPlayer View's
    private ImageView imageViewAlbumArt;
    private Button buttonPrevious;
    private Button buttonPause;
    private Button buttonStop;


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

        // Set Listener
        imageViewAlbumArt.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NAVIGATION_VIEW_ITEM_SELECTED, navigationViewItemSelected);
        outState.putBoolean(KEY_MINI_PLAYER_GONE, miniPlayerGone);
        outState.putInt(KEY_MINI_PLAYER_TRACK_POSITION, miniPlayerTrackPosition);
    }

    private void setInstanceState(Bundle bundle) {
        if (bundle == null) {
            // Mini Player Gone
            miniPlayerGone = true;

            // Navigation View
            navigationViewItemSelected = R.id.menu_navigation_view_item_tracks;
        } else {
            // MiniPlayer
            miniPlayerGone = bundle.getBoolean(KEY_MINI_PLAYER_GONE);
            miniPlayerTrackPosition = bundle.getInt(KEY_MINI_PLAYER_TRACK_POSITION);
            setMiniPlayerTitle(TrackList.getInstance().getTrackList().get(miniPlayerTrackPosition).getTitle());

            // Navigation View
            navigationViewItemSelected = bundle.getInt(KEY_NAVIGATION_VIEW_ITEM_SELECTED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
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
            actionBar.setSubtitle(R.string.app_name);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(activityFrameLayout.getId(), listFragment);
        if (isLandscapeOrientedTablet) {
            //fragmentTransaction.replace(, optionalFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        navigationViewItemSelected = item.getItemId();
        setFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAlbumListItemClick(String album, int albumId) {
        Toast.makeText(this, album + String.valueOf(albumId), Toast.LENGTH_SHORT).show();

        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        TrackListFragment trackListFragment = new TrackListFragment();
        fragmentTransaction.addToBackStack("Album");
        fragmentTransaction.replace(activityFrameLayout.getId(), trackListFragment);

        fragmentTransaction.commit();

    }

    @Override
    public void onArtistListItemClick(int artistId) {
        Toast.makeText(this, String.valueOf(artistId), Toast.LENGTH_SHORT).show();

        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        TrackListFragment trackListFragment = new TrackListFragment();
        fragmentTransaction.addToBackStack("Artist");
        fragmentTransaction.replace(activityFrameLayout.getId(), trackListFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onPlaylistListItemClick(int playlistId) {
        Toast.makeText(this,String.valueOf(playlistId), Toast.LENGTH_SHORT).show();

        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        TrackListFragment trackListFragment = new TrackListFragment();
        fragmentTransaction.addToBackStack("Playlist");
        fragmentTransaction.replace(activityFrameLayout.getId(), trackListFragment);

        fragmentTransaction.commit();

    @Subscribe
    public void onEvent(StartMiniPlayerEvent event) {
        // Visible MiniPlayer
        setMiniPlayerGone(false);

        // Set Track Position
        miniPlayerTrackPosition = event.getPosition();

        // Start MusicService
        Intent musicServiceIntent = new Intent(getApplicationContext(), MusicService.class);
        musicServiceIntent.putExtra(KEY_NAVIGATION_VIEW_ITEM_SELECTED, navigationViewItemSelected);
        musicServiceIntent.putExtra(KEY_MINI_PLAYER_TRACK_POSITION, miniPlayerTrackPosition);
        startService(musicServiceIntent);

        // Set Track Title
        setMiniPlayerTitle(TrackList.getInstance().getTrackList().get(event.getPosition()).getTitle());
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

    private void setMiniPlayerTitle(String trackTitle) {
        TextView title = (TextView) findViewById(R.id.activity_mini_player_title);
        title.setText(trackTitle);
        title.setSelected(true);
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
                eventBus.post(new StopMiniPlayerEvent(miniPlayerTrackPosition));
                setMiniPlayerGone(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }
}
