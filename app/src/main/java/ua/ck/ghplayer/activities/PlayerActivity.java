package ua.ck.ghplayer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.events.PlayerButtonEvent;
import ua.ck.ghplayer.events.PlayerUpdateEvent;
import ua.ck.ghplayer.events.TouchProgressBarEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.events.UpdateTrackContentEvent;
import ua.ck.ghplayer.lists.FavoriteTrackList;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.TimeUtils;

public class PlayerActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    // Track List & Track position
    private int trackListId;
    private int trackPosition;

    // Instances
    private EventBus eventBus = EventBus.getDefault();

    // View's
    private SeekBar seekBar;
    private long trackIntentCurrentDuration;
    private long trackIntentTotalDuration;
    private TextView trackTimeValue;
    private TextView trackElapsedTimeValue;
    private TextView trackTitle;
    private TextView trackArtist;
    private TextView trackAlbum;
    private ImageView trackAlbumArt;

    // Button's
    private boolean buttonPauseVisible;
    private Button buttonPlay;
    private Button buttonPause;
    private Button buttonNext;
    private Button buttonPrevious;

    // ---------------------------------------------------------------------------------------------
    // EventBus Register
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        // Send Player Status In Music Service
        eventBus.post(new PlayerUpdateEvent(Constants.PLAYER_STATUS_START));
    }

    // ---------------------------------------------------------------------------------------------
    // Main Set's
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getIntentExtra();
        setInstanceState(savedInstanceState);
        setView();
        setPlayerTrackContent(trackListId, trackPosition);
    }

    // ---------------------------------------------------------------------------------------------
    // Get Data From Activity
    // ---------------------------------------------------------------------------------------------

    private void getIntentExtra() {
        this.trackListId = getIntent().getExtras().getInt(Constants.MINI_PLAYER_TRACK_LIST_ID_KEY);
        Log.i("PLAYER", "TRACK LIST: " + String.valueOf(trackListId));
        this.trackPosition = getIntent().getExtras().getInt(Constants.MINI_PLAYER_TRACK_POSITION_KEY);
        this.trackIntentCurrentDuration = getIntent().getExtras().getLong(Constants.PLAYER_UPDATE_TRACK_CURRENT_DURATION);
        this.trackIntentTotalDuration = getIntent().getExtras().getLong(Constants.PLAYER_UPDATE_TRACK_TOTAL_DURATION);
        this.buttonPauseVisible = getIntent().getExtras().getBoolean(Constants.ACTIVITY_RESULT_STATUS_BUTTON_PAUSE_INTENT_KEY);
    }

    // ---------------------------------------------------------------------------------------------
    // Find View's & Set Listener
    // ---------------------------------------------------------------------------------------------

    private void setView() {
        // Find & Set Track Time Info View's
        trackElapsedTimeValue = (TextView) findViewById(R.id.activity_player_container_duration_track_elapsed_time_value);
        trackElapsedTimeValue.setText(TimeUtils.milliSecondsConverter(trackIntentCurrentDuration));
        trackTimeValue = (TextView) findViewById(R.id.activity_player_container_duration_track_time_value);
        trackTimeValue.setText(TimeUtils.milliSecondsConverter(trackIntentTotalDuration));

        // Find Track Info View's
        trackAlbumArt = (ImageView) findViewById(R.id.activity_player_container_album_art_image);
        trackTitle = (TextView) findViewById(R.id.activity_player_container_track_info_title);
        trackArtist = (TextView) findViewById(R.id.activity_player_container_track_info_artist);
        trackAlbum = (TextView) findViewById(R.id.activity_player_container_track_info_album);

        // Find & Set SeekBar
        seekBar = (SeekBar) findViewById(R.id.activity_player_track_seekbar);
        seekBar.setMax((int) trackIntentTotalDuration);
        seekBar.setProgress((int) trackIntentCurrentDuration);
        seekBar.setOnSeekBarChangeListener(this);

        // Find Button's
        buttonPlay = (Button) findViewById(R.id.activity_player_container_track_control_button_play);
        buttonPause = (Button) findViewById(R.id.activity_player_container_track_control_button_pause);
        buttonNext = (Button) findViewById(R.id.activity_player_container_track_control_button_next);
        buttonPrevious = (Button) findViewById(R.id.activity_player_container_track_control_button_previous);

        // Set Listener's
        buttonPlay.setOnClickListener(this);
        buttonPause.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonPrevious.setOnClickListener(this);

        // Set Button Pause Status
        if (buttonPauseVisible) {
            buttonPause.setVisibility(View.VISIBLE);
            buttonPlay.setVisibility(View.GONE);
        } else {
            buttonPause.setVisibility(View.GONE);
            buttonPlay.setVisibility(View.VISIBLE);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Save & Set Player Activity State
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Player current state
        outState.putInt(Constants.PLAYER_TRACK_LIST_ID_KEY, trackListId);
        outState.putInt(Constants.PLAYER_TRACK_POSITION_KEY, trackPosition);
        outState.putBoolean(Constants.BUTTON_PAUSE_VISIBLE_STATUS, buttonPauseVisible);
    }

    private void setInstanceState(Bundle bundle) {
        if (bundle != null) {
            trackListId = bundle.getInt(Constants.PLAYER_TRACK_LIST_ID_KEY);
            trackPosition = bundle.getInt(Constants.PLAYER_TRACK_POSITION_KEY);
            buttonPauseVisible = bundle.getBoolean(Constants.BUTTON_PAUSE_VISIBLE_STATUS);
            setActivityResultIntent();
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
    // Player Track Content
    // ---------------------------------------------------------------------------------------------

    private void setPlayerTrackContent(int trackListId, int trackPosition) {
        // Update TrackList & TrackPosition
        this.trackListId = trackListId;
        this.trackPosition = trackPosition;

        Uri getAlbumArtUri = null;
        String getTrackTitle = null;
        String getTrackArtist = null;
        String getTrackAlbum = null;

        switch (trackListId) {
            case (Constants.MAIN_TRACK_LIST_ID):
                getAlbumArtUri = TrackList.getInstance().getTrackList().get(trackPosition).getAlbumArt();
                getTrackTitle = TrackList.getInstance().getTrackList().get(trackPosition).getTitle();
                getTrackArtist = TrackList.getInstance().getTrackList().get(trackPosition).getArtist();
                getTrackAlbum = TrackList.getInstance().getTrackList().get(trackPosition).getAlbum();
                break;
            case (Constants.ALBUM_TRACK_LIST_ID):
                // ADD
                break;
            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                // ADD
                break;
            case (Constants.GENRE_TRACK_LIST_ID):
                getAlbumArtUri = GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumArt();
                getTrackTitle = GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle();
                getTrackArtist = GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist();
                getTrackAlbum = GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbum();
                break;
            case (Constants.FAVORITE_TRACK_LIST_ID):
                getAlbumArtUri = FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getAlbumArt();
                getTrackTitle = FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getTitle();
                getTrackArtist = FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getArtist();
                getTrackAlbum = FavoriteTrackList.getInstance().getFavoriteTrackList().get(trackPosition).getAlbum();
                break;
        }

        trackTitle.setText(getTrackTitle);
        trackTitle.setSelected(true);
        trackArtist.setText(getTrackArtist);
        trackAlbum.setText(getTrackAlbum);

        if (getAlbumArtUri != null) {
            Picasso.with(getApplicationContext())
                    .load(getAlbumArtUri)
                    .into(trackAlbumArt);
        } else {
            Picasso.with(getApplicationContext())
                    .load(R.drawable.album_cover_default_big)
                    .into(trackAlbumArt);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Update Player Track Content
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onUpdateTrackContentEvent(UpdateTrackContentEvent event) {
        // Always update TrackList Id & Track Position
        this.trackListId = event.getTrackListId();
        this.trackPosition = event.getTrackPosition();
        // Update Player
        setPlayerTrackContent(event.getTrackListId(), event.getTrackPosition());
        // Set Activity Result
        setActivityResultIntent();
    }

    // ---------------------------------------------------------------------------------------------
    // ProgressBar & TrackTime
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onUpdateProgressBarEvent(UpdateProgressBarEvent updateProgressBarEvent) {
        long trackCurrentDuration = updateProgressBarEvent.getTrackCurrentDuration();
        long trackTotalDuration = updateProgressBarEvent.getTrackTotalDuration();

        trackElapsedTimeValue.setText(TimeUtils.milliSecondsConverter(trackCurrentDuration));
        seekBar.setProgress((int) trackCurrentDuration);
        trackTimeValue.setText(TimeUtils.milliSecondsConverter(trackTotalDuration));
        seekBar.setMax((int) trackTotalDuration);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Seekbar
        trackElapsedTimeValue.setText(TimeUtils.milliSecondsConverter(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Seekbar
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Seekbar
        eventBus.post(new TouchProgressBarEvent(seekBar.getProgress()));
    }

    // ---------------------------------------------------------------------------------------------
    // Set Button Pause Active
    // ---------------------------------------------------------------------------------------------

    void setPlayerButtonPauseActive() {
        buttonPauseVisible = true;
        setActivityResultIntent();

        // Set Active Button Pause
        buttonPause.setVisibility(View.VISIBLE);
        buttonPlay.setVisibility(View.GONE);

        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PLAY));
    }

    // ---------------------------------------------------------------------------------------------
    // Player Button Click
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.activity_player_container_track_control_button_previous):
                onClickPlayerButtonPrevious();
                break;

            case (R.id.activity_player_container_track_control_button_play):
                onClickPlayerButtonPlay();
                break;

            case (R.id.activity_player_container_track_control_button_pause):
                onClickPlayerButtonPause();
                break;

            case (R.id.activity_player_container_track_control_button_next):
                onClickPlayerButtonNext();
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Previous Button Player
    // ---------------------------------------------------------------------------------------------

    private void onClickPlayerButtonPrevious() {
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS));
        // Set Button Pause Active
        if (buttonPause.getVisibility() == View.GONE) {
            setPlayerButtonPauseActive();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Play Button Player
    // ---------------------------------------------------------------------------------------------

    private void onClickPlayerButtonPlay() {
        buttonPauseVisible = true;
        setActivityResultIntent();
        // Replace Buttons
        buttonPlay.setVisibility(View.GONE);
        buttonPause.setVisibility(View.VISIBLE);
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PLAY));
    }

    // -------------------------------------------------------------------------------------
    // Pause Button Player
    // -------------------------------------------------------------------------------------

    private void onClickPlayerButtonPause() {
        buttonPauseVisible = false;
        setActivityResultIntent();
        // Replace Buttons
        buttonPause.setVisibility(View.GONE);
        buttonPlay.setVisibility(View.VISIBLE);
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE));
    }

    // -------------------------------------------------------------------------------------
    // Next Button Player
    // -------------------------------------------------------------------------------------

    private void onClickPlayerButtonNext() {
        // Update Notification Player
        eventBus.post(new NotificationPlayerEvent(Constants.PLAYER_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_NEXT));
        // Set Button Pause Active
        if (buttonPause.getVisibility() == View.GONE) {
            setPlayerButtonPauseActive();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Player Button Event - Notification CallBack
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onPlayerButtonEvent(PlayerButtonEvent event) {
        switch (event.getClickButton()) {
            case (Constants.PLAYER_BUTTON_PREVIOUS):
                // Set Button Pause Active
                if (buttonPause.getVisibility() == View.GONE) {
                    setPlayerButtonPauseActive();
                }
                break;

            case (Constants.PLAYER_BUTTON_PLAY):
                buttonPauseVisible = true;
                setActivityResultIntent();
                // Replace Buttons
                buttonPlay.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
                break;

            case (Constants.PLAYER_BUTTON_PAUSE):
                buttonPauseVisible = false;
                setActivityResultIntent();
                // Replace Buttons
                buttonPause.setVisibility(View.GONE);
                buttonPlay.setVisibility(View.VISIBLE);
                break;

            case (Constants.PLAYER_BUTTON_NEXT):
                // Set Button Pause Active
                if (buttonPause.getVisibility() == View.GONE) {
                    setPlayerButtonPauseActive();
                }
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // EventBus Unregister
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onStop() {
        // Send Player Status In Music Service
        eventBus.post(new PlayerUpdateEvent(Constants.PLAYER_STATUS_STOP));
        eventBus.unregister(this);
        super.onStop();
    }
}
