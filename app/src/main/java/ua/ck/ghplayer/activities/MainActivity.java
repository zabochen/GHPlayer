package ua.ck.ghplayer.activities;

import android.content.Intent;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.events.MiniPlayerButtonEvent;
import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.events.ShowTrackListActivity;
import ua.ck.ghplayer.events.StartMiniPlayerEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.events.UpdateTrackContentEvent;
import ua.ck.ghplayer.fragments.AlbumListFragment;
import ua.ck.ghplayer.fragments.ArtistListFragment;
import ua.ck.ghplayer.fragments.FavoriteTrackListFragment;
import ua.ck.ghplayer.fragments.GenreListFragment;
import ua.ck.ghplayer.fragments.PlaylistListFragment;
import ua.ck.ghplayer.fragments.TrackListFragment;
import ua.ck.ghplayer.lists.FavoriteTrackList;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.services.MusicService;
import ua.ck.ghplayer.utils.Constants;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    // View's
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    // Main Configuration
    private int trackListId;
    private int trackPosition;

    // NavigationView
    private static int navigationViewItemSelected;

    // MiniPlayer
    private boolean miniPlayerGone;
    private boolean buttonPauseVisible;
    private RelativeLayout miniPlayer;
    private ImageView imageViewAlbumArt;
    private TextView title;
    private Button buttonPrevious;
    private Button buttonPlay;
    private Button buttonPause;
    private Button buttonStop;
    private Button buttonNext;

    // Player
    private UpdateProgressBarEvent updateProgressBarEvent;
    private long trackCurrentDuration;
    private long trackTotalDuration;

    // EventBus Instance
    private EventBus eventBus = EventBus.getDefault();

    // ---------------------------------------------------------------------------------------------
    // EventBus Register
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    // ---------------------------------------------------------------------------------------------
    // Main Set's
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setInstanceState(savedInstanceState);
        setToolbar();
        setNavigationView();
        setMiniPlayerGone(miniPlayerGone);
        setFragment();
    }

    // ---------------------------------------------------------------------------------------------
    // Find View's & Set Listener
    // ---------------------------------------------------------------------------------------------

    private void setView() {
        // Find View
        imageViewAlbumArt = (ImageView) findViewById(R.id.activity_mini_player_album_art);
        title = (TextView) findViewById(R.id.activity_mini_player_title);
        buttonPrevious = (Button) findViewById(R.id.activity_mini_player_button_previous);
        buttonPlay = (Button) findViewById(R.id.activity_mini_player_button_play);
        buttonPause = (Button) findViewById(R.id.activity_mini_player_button_pause);
        buttonStop = (Button) findViewById(R.id.activity_mini_player_button_stop);
        buttonNext = (Button) findViewById(R.id.activity_mini_player_button_next);

        // Set Listener
        imageViewAlbumArt.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        buttonPause.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
    }

    // ---------------------------------------------------------------------------------------------
    // Save & Set Activity State
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Main Data
        outState.putInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY, trackListId);
        outState.putInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);

        // Save MiniPlayer current state
        outState.putBoolean(Constants.MINI_PLAYER_GONE_KEY, miniPlayerGone);
        outState.putBoolean(Constants.BUTTON_PAUSE_VISIBLE_STATUS, buttonPauseVisible);
    }

    private void setInstanceState(Bundle bundle) {
        if (bundle == null) {
            // Mini Player Gone
            miniPlayerGone = true;

            // Navigation View
            navigationViewItemSelected = R.id.menu_navigation_view_item_tracks;
        } else {
            // Set Main Data
            trackListId = bundle.getInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY);
            trackPosition = bundle.getInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY);

            // Set MiniPlayer
            miniPlayerGone = bundle.getBoolean(Constants.MINI_PLAYER_GONE_KEY);
            setMiniPlayerTrackContent(trackListId, trackPosition);

            // Set Button Pause - Gone/Visible
            if (!miniPlayerGone) {
                buttonPauseVisible = bundle.getBoolean(Constants.BUTTON_PAUSE_VISIBLE_STATUS);
                if (buttonPauseVisible) {
                    buttonPlay.setVisibility(View.GONE);
                    buttonPause.setVisibility(View.VISIBLE);
                } else {
                    buttonPause.setVisibility(View.GONE);
                    buttonPlay.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Set Toolbar & Fragment
    // ---------------------------------------------------------------------------------------------

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

    private void setFragment() {
        FrameLayout activityFrameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        Fragment listFragment = null;

        switch (navigationViewItemSelected) {
            case R.id.menu_navigation_view_item_tracks:
                listFragment = new TrackListFragment();
                break;
            case R.id.menu_navigation_view_item_albums:
                listFragment = new AlbumListFragment();
                break;
            case R.id.menu_navigation_view_item_artists:
                listFragment = new ArtistListFragment();
                break;
            case R.id.menu_navigation_view_item_genres:
                listFragment = new GenreListFragment();
                break;
            case R.id.menu_navigation_view_item_favorites:
                listFragment = new FavoriteTrackListFragment();
                break;
            default:
                break;
        }

        // Replace Fragment's
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(activityFrameLayout.getId(), listFragment)
                .commit();
    }

    // ---------------------------------------------------------------------------------------------
    // NavigationView: Set & Listener
    // ---------------------------------------------------------------------------------------------

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        navigationViewItemSelected = item.getItemId();
        setFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
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

    // ---------------------------------------------------------------------------------------------
    // Show Track List Activity Base
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onShowTrackListActivity(ShowTrackListActivity event) {

        switch (event.getTrackListId()) {
            case (Constants.ALBUM_TRACK_LIST_ID):
                showAlbumTrackListActivity(event);
                break;

            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                showArtistAlbumTrackListActivity(event);
                break;

            case (Constants.GENRE_TRACK_LIST_ID):
                showGenreTrackListActivity(event);
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Show Album Track List Activity
    // ---------------------------------------------------------------------------------------------

    public void showAlbumTrackListActivity(ShowTrackListActivity event) {
        // ADD
    }

    // ---------------------------------------------------------------------------------------------
    // Show Artist Album Track List Activity
    // ---------------------------------------------------------------------------------------------

    public void showArtistAlbumTrackListActivity(ShowTrackListActivity event) {
        // ADD
    }

    // ---------------------------------------------------------------------------------------------
    // Show Genre Track List Activity
    // ---------------------------------------------------------------------------------------------

    public void showGenreTrackListActivity(ShowTrackListActivity event) {
        Intent genreTrackListActivityIntent = new Intent(getApplicationContext(), GenreTrackListActivity.class);
        // MiniPlayer current state (Gone/Visible)
        genreTrackListActivityIntent.putExtra(Constants.MINI_PLAYER_GONE_KEY, miniPlayerGone);
        // Selected Genre Position
        genreTrackListActivityIntent.putExtra(Constants.ACTIVITY_RESULT_ITEM_POSITION_INTENT_KEY, event.getPosition());
        // If MiniPlayer Visible
        if (!miniPlayerGone) {
            // Current TrackList ID
            genreTrackListActivityIntent.putExtra(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY, trackListId);
            // Current Track position
            genreTrackListActivityIntent.putExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
            // Button Pause Status
            genreTrackListActivityIntent.putExtra(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY, buttonPauseVisible);
        }
        // requestCode = TrackList ID
        startActivityForResult(genreTrackListActivityIntent, event.getTrackListId());
    }

    // ---------------------------------------------------------------------------------------------
    // MiniPlayer Start Event
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onStartMiniPlayerEvent(StartMiniPlayerEvent event) {

        this.trackListId = event.getTrackListId();
        this.trackPosition = event.getPosition();

        // Update Button Pause
        setMiniPlayerButtonPauseActive();

        // Visible MiniPlayer
        if (miniPlayer.getVisibility() == View.GONE) {
            setMiniPlayerGone(false);
        }

        // Start MusicService
        Intent musicServiceStartIntent = new Intent(getApplicationContext(), MusicService.class);
        musicServiceStartIntent.putExtra(Constants.TRACK_LIST_ID_KEY, trackListId);
        musicServiceStartIntent.putExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
        startService(musicServiceStartIntent);

        // Set MiniPlayer track content
        setMiniPlayerTrackContent(trackListId, trackPosition);
    }

    // ---------------------------------------------------------------------------------------------
    // Update MiniPlayer Content
    // ---------------------------------------------------------------------------------------------

    private void setMiniPlayerTrackContent(int trackListId, int trackPosition) {
        Uri albumArtUri = null;
        StringBuilder stringBuilder = new StringBuilder();

        switch (trackListId) {
            case (Constants.MAIN_TRACK_LIST_ID):
                stringBuilder.append(TrackList.getInstance().getTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(TrackList.getInstance().getTrackList().get(trackPosition).getTitle());
                albumArtUri = TrackList.getInstance().getTrackList().get(trackPosition).getAlbumArt();
                break;

            case (Constants.ALBUM_TRACK_LIST_ID):
                // ADD
                break;

            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                // ADD
                break;

            case (Constants.GENRE_TRACK_LIST_ID):
                stringBuilder.append(GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle());
                albumArtUri = GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumArt();
                break;

            case (Constants.FAVORITE_TRACK_LIST_ID):
                stringBuilder.append(FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getTitle());
                albumArtUri = FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getAlbumArt();
                break;
        }

        // Set Title
        title.setText(stringBuilder.toString());
        title.setSelected(true);

        // Set AlbumArt
        if (albumArtUri != null) {
            Picasso.with(getApplicationContext())
                    .load(albumArtUri)
                    .into(imageViewAlbumArt);
        } else {
            Picasso.with(getApplicationContext())
                    .load(R.drawable.album_cover_default)
                    .into(imageViewAlbumArt);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Update Mini Player Track Content - Notification CallBack
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onUpdateTrackContentEvent(UpdateTrackContentEvent event) {
        // Always update TrackList Id & Track Position
        this.trackListId = event.getTrackListId();
        this.trackPosition = event.getTrackPosition();
        // Update MiniPlayer
        setMiniPlayerTrackContent(event.getTrackListId(), event.getTrackPosition());
    }

    // ---------------------------------------------------------------------------------------------
    // MiniPlayer Status - Gone/Visible
    // ---------------------------------------------------------------------------------------------

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

    // ---------------------------------------------------------------------------------------------
    // Update Button Pause Status - Gone/Visible
    // ---------------------------------------------------------------------------------------------

    void setMiniPlayerButtonPauseActive() {
        // Set active Mini Player button Pause
        buttonPauseVisible = true;
        buttonPause.setVisibility(View.VISIBLE);
        buttonPlay.setVisibility(View.GONE);

        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.MAIN_ACTIVITY_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PLAY));
    }

    // ---------------------------------------------------------------------------------------------
    // Processing MiniPlayer Button Click
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.activity_mini_player_album_art):
                onStartPlayer(trackListId, trackPosition);
                break;
            case (R.id.activity_mini_player_button_previous):
                onClickMiniPlayerButtonPrevious();
                break;
            case (R.id.activity_mini_player_button_play):
                onClickMiniPlayerButtonPlay();
                break;
            case (R.id.activity_mini_player_button_pause):
                onClickMiniPlayerButtonPause();
                break;
            case (R.id.activity_mini_player_button_stop):
                onClickMiniPlayerButtonStop();
                break;
            case (R.id.activity_mini_player_button_next):
                onClickMiniPlayerButtonNext();
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Previous Button Mini Player
    // ---------------------------------------------------------------------------------------------

    private void onClickMiniPlayerButtonPrevious() {
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.MINI_PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS));
        // Update Button Pause
        if (buttonPause.getVisibility() == View.GONE) {
            setMiniPlayerButtonPauseActive();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Play Button Mini Player
    // ---------------------------------------------------------------------------------------------

    private void onClickMiniPlayerButtonPlay() {
        // Replace Buttons
        buttonPauseVisible = true;
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.MINI_PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PLAY));
    }

    // ---------------------------------------------------------------------------------------------
    // Stop Button Mini Player
    // ---------------------------------------------------------------------------------------------

    private void onClickMiniPlayerButtonStop() {
        // Hide Mini Player
        setMiniPlayerGone(true);
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.MINI_PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_STOP));
    }

    // ---------------------------------------------------------------------------------------------
    // Pause Button Mini Player
    // ---------------------------------------------------------------------------------------------

    private void onClickMiniPlayerButtonPause() {
        // Replace Buttons
        buttonPauseVisible = false;
        buttonPause.setVisibility(View.GONE);
        buttonPlay.setVisibility(View.VISIBLE);
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.MINI_PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE));
    }

    // ---------------------------------------------------------------------------------------------
    // Previous Button Mini Player
    // ---------------------------------------------------------------------------------------------

    private void onClickMiniPlayerButtonNext() {
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.MINI_PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_NEXT));
        // Update Button Pause
        if (buttonPause.getVisibility() == View.GONE) {
            setMiniPlayerButtonPauseActive();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Mini Player Button Event - Notification CallBack
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onMiniPlayerButtonEvent(MiniPlayerButtonEvent event) {
        switch (event.getClickButton()) {
            case (Constants.MINI_PLAYER_BUTTON_PREVIOUS):
                // Update Button Pause
                if (buttonPause.getVisibility() == View.GONE) {
                    setMiniPlayerButtonPauseActive();
                }
                break;

            case (Constants.MINI_PLAYER_BUTTON_PLAY):
                buttonPauseVisible = true;
                buttonPause.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.GONE);
                break;

            case (Constants.MINI_PLAYER_BUTTON_PAUSE):
                buttonPauseVisible = false;
                buttonPause.setVisibility(View.GONE);
                buttonPlay.setVisibility(View.VISIBLE);
                break;

            case (Constants.MINI_PLAYER_BUTTON_STOP):
                switch (event.getSenderId()) {
                    case (Constants.NOTIFICATION_PLAYER_SENDER_ID):
                        // Hide Mini Player
                        setMiniPlayerGone(true);
                        break;
                }
                break;

            case (Constants.MINI_PLAYER_BUTTON_NEXT):
                // Update Button Pause
                if (buttonPause.getVisibility() == View.GONE) {
                    setMiniPlayerButtonPauseActive();
                }
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Start Player Activity
    // ---------------------------------------------------------------------------------------------

    public void onStartPlayer(int trackListId, int trackPosition) {

        Intent startPlayerIntent = new Intent(getApplicationContext(), PlayerActivity.class);

        // MiniPlayer current state (Gone/Visible)
        startPlayerIntent.putExtra(Constants.MINI_PLAYER_GONE_KEY, miniPlayerGone);

        // If MiniPlayer Visible
        if (!miniPlayerGone) {
            // Current TrackList ID
            startPlayerIntent.putExtra(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY, trackListId);
            // Current Track position
            startPlayerIntent.putExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, trackPosition);
            // Status button Pause
            startPlayerIntent.putExtra(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY, buttonPauseVisible);
        }

        // Track Duration
        if (updateProgressBarEvent != null) {
            startPlayerIntent.putExtra(Constants.PLAYER_UPDATE_TRACK_CURRENT_DURATION, trackCurrentDuration);
            startPlayerIntent.putExtra(Constants.PLAYER_UPDATE_TRACK_TOTAL_DURATION, trackTotalDuration);
        }

        // Start Player Activity & RequestCode
        startActivityForResult(startPlayerIntent, 18);
    }

    // ---------------------------------------------------------------------------------------------
    // Set Player Before The Start
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onUpdateProgressBarEvent(UpdateProgressBarEvent updateProgressBarEvent) {
        if (updateProgressBarEvent != null) {
            this.updateProgressBarEvent = updateProgressBarEvent;
            this.trackCurrentDuration = updateProgressBarEvent.getTrackCurrentDuration();
            this.trackTotalDuration = updateProgressBarEvent.getTrackTotalDuration();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Activity Result - Get Data With Player Activity
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            this.miniPlayerGone = data.getExtras().getBoolean(Constants.MINI_PLAYER_GONE_KEY);
            this.trackListId = data.getExtras().getInt(Constants.ACTIVITY_RESULT_TRACK_LIST_ID_INTENT_KEY);
            this.trackPosition = data.getExtras().getInt(Constants.ACTIVITY_RESULT_TRACK_POSITION_INTENT_KEY);
            this.buttonPauseVisible = data.getExtras().getBoolean(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY);

            // Update Mini Player
            setMiniPlayerGone(miniPlayerGone);
            setMiniPlayerTrackContent(trackListId, trackPosition);
            // Set button Pause Status
            if (buttonPauseVisible) {
                buttonPause.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.GONE);
            } else {
                buttonPause.setVisibility(View.GONE);
                buttonPlay.setVisibility(View.VISIBLE);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // EventBus Unregister
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

}
