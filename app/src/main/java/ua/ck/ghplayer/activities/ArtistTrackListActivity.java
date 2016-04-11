package ua.ck.ghplayer.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.adapters.ArtistTrackListAdapter;
import ua.ck.ghplayer.events.MiniPlayerButtonEvent;
import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.events.UpdateTrackContentEvent;
import ua.ck.ghplayer.interfaces.ItemClickListener;
import ua.ck.ghplayer.listeners.RecyclerViewTouchListener;
import ua.ck.ghplayer.lists.AlbumTrackList;
import ua.ck.ghplayer.lists.ArtistList;
import ua.ck.ghplayer.lists.ArtistTrackList;
import ua.ck.ghplayer.lists.FavoriteTrackList;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.loaders.ArtistTrackListLoader;
import ua.ck.ghplayer.services.MusicService;
import ua.ck.ghplayer.utils.Constants;

public class ArtistTrackListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ItemClickListener,
        View.OnClickListener {

    private RecyclerView artistTrackListRecyclerView;
    private ArtistTrackListAdapter artistTrackListAdapter;

    // Artist ID
    private int artistPosition;
    private long artistId;

    // Main Configuration
    private int trackListId;
    private int trackPosition;

    // View's
    private Toolbar toolbar;

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

    // Event Bus Instance
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
        setContentView(R.layout.activity_track_list);
        getIntentExtra();
        getSupportLoaderManager().initLoader(Constants.ARTIST_ALBUM_TRACK_LIST_LOADER_ID, null, this);
        setView();
        setToolbar();
    }

    // ---------------------------------------------------------------------------------------------
    // Get Data From Activity
    // ---------------------------------------------------------------------------------------------

    private void getIntentExtra() {
        this.artistPosition = getIntent().getExtras().getInt(Constants.ACTIVITY_RESULT_ITEM_POSITION_INTENT_KEY);
        this.artistId = ArtistList.getInstance().getArtistList().get(artistPosition).getId();
        this.miniPlayerGone = getIntent().getExtras().getBoolean(Constants.MINI_PLAYER_GONE_KEY);
        if (!miniPlayerGone) {
            this.trackListId = getIntent().getExtras().getInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY);
            this.trackPosition = getIntent().getExtras().getInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY);
            this.buttonPauseVisible = getIntent().getExtras().getBoolean(Constants.BUTTON_PAUSE_VISIBLE_STATUS);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Find View's & Set Listener
    // ---------------------------------------------------------------------------------------------

    private void setView() {
        // MiniPlayer
        setMiniPlayerGone(miniPlayerGone);
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

        // MiniPlayer Update Content & Replace Buttons
        if (!miniPlayerGone) {
            setMiniPlayerTrackContent(trackListId, trackPosition);
            if (buttonPauseVisible) {
                buttonPlay.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
            }
        }

        // RecyclerView - Base Configuration
        artistTrackListRecyclerView = (RecyclerView) findViewById(R.id.activity_track_list_recycler_view);
        artistTrackListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        artistTrackListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        artistTrackListRecyclerView.setHasFixedSize(true);

        // RecyclerView - Add TouchListener
        RecyclerViewTouchListener artistTrackListListener = new RecyclerViewTouchListener(
                getApplicationContext(), this, artistTrackListRecyclerView);
        artistTrackListRecyclerView.addOnItemTouchListener(artistTrackListListener);

        // RecyclerView - Set Adapter
        artistTrackListAdapter = new ArtistTrackListAdapter();
        artistTrackListRecyclerView.setAdapter(artistTrackListAdapter);
    }

    // ---------------------------------------------------------------------------------------------
    // Set Toolbar
    // ---------------------------------------------------------------------------------------------

    private void setToolbar() {
        // Toolbar
        this.toolbar = (Toolbar) findViewById(R.id.activity_track_list_toolbar);
        setSupportActionBar(toolbar);

        // Actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(ArtistList.getInstance().getArtistList().get(artistPosition).getArtist());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Home Button
    // ---------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    // Loader ArtistTrackList
    // ---------------------------------------------------------------------------------------------

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //  Before load new ArtistTrackList, clear old ArtistTrackList
        if (ArtistTrackList.getInstance().getArtistTrackList() != null) {
            ArtistTrackList.getInstance().getArtistTrackList().clear();
        }
        return new ArtistTrackListLoader(getApplicationContext(), artistId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Set ArtistTrackList
        if (data != null && data.moveToFirst()) {
            ArtistTrackList.getInstance().setArtistTrackList(getApplicationContext(), data);
            artistTrackListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    // ---------------------------------------------------------------------------------------------
    // Click Listener
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view, int position) {

        // Save Current Artist TrackList & Track Position
        ArtistTrackList.getInstance().saveArtistTrackList();
        this.trackListId = Constants.ARTIST_ALBUM_TRACK_LIST_ID;
        this.trackPosition = position;

        // Visible MiniPlayer & Set Content & Set Button Pause
        if (miniPlayer.getVisibility() == View.GONE) {
            setMiniPlayerGone(false);
        }
        setMiniPlayerTrackContent(Constants.ARTIST_ALBUM_TRACK_LIST_ID, trackPosition);
        setMiniPlayerButtonPauseActive();

        // Start MusicService
        Intent musicServiceStartIntent = new Intent(getApplicationContext(), MusicService.class);
        musicServiceStartIntent.putExtra(Constants.TRACK_LIST_ID_KEY, Constants.ARTIST_ALBUM_TRACK_LIST_ID);
        musicServiceStartIntent.putExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, position);
        startService(musicServiceStartIntent);

        // Set Activity Result
        setActivityResultIntent();
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
                stringBuilder.append(AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getTitle());
                albumArtUri = AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getAlbumArt();
                break;

            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                stringBuilder.append(ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getTitle());
                albumArtUri = ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getAlbumArt();
                break;

            case (Constants.GENRE_TRACK_LIST_ID):
                stringBuilder.append(GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle());
                albumArtUri = GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumArt();
                break;

            case (Constants.FAVORITE_TRACK_LIST_ID):
                stringBuilder.append(FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getArtist());
                stringBuilder.append(" - ");
                stringBuilder.append(FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getTitle());
                albumArtUri = FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getAlbumArt();
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
        // Set Activity Result
        setActivityResultIntent();
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
        buttonPauseVisible = true;
        setActivityResultIntent();

        // Set active Mini Player button Pause
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
        buttonPauseVisible = true;
        setActivityResultIntent();
        // Replace Buttons
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
        buttonPauseVisible = false;
        setActivityResultIntent();
        // Replace Buttons
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
                setActivityResultIntent();
                // Replace Buttons
                buttonPause.setVisibility(View.VISIBLE);
                buttonPlay.setVisibility(View.GONE);
                break;

            case (Constants.MINI_PLAYER_BUTTON_PAUSE):
                buttonPauseVisible = false;
                setActivityResultIntent();
                // Replace Buttons
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
    // Set Player Activity Result
    // ---------------------------------------------------------------------------------------------

    public void setActivityResultIntent() {
        Intent sendIntent = new Intent();
        sendIntent.putExtra(Constants.MINI_PLAYER_GONE_KEY, false);
        sendIntent.putExtra(Constants.ACTIVITY_RESULT_TRACK_LIST_ID_INTENT_KEY, trackListId);
        sendIntent.putExtra(Constants.ACTIVITY_RESULT_TRACK_POSITION_INTENT_KEY, trackPosition);
        sendIntent.putExtra(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY, buttonPauseVisible);
        setResult(RESULT_OK, sendIntent);
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

    @Override
    public void onLongClick(View view, int position) {

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
