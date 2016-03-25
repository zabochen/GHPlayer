package ua.ck.ghplayer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.events.PausePlayerEvent;
import ua.ck.ghplayer.events.PlayPausePlayerEvent;
import ua.ck.ghplayer.events.PlayerUpdateEvent;
import ua.ck.ghplayer.events.TouchProgressBarEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.TimeUtils;

public class PlayerActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    // Instances
    private EventBus eventBus = EventBus.getDefault();

    // View's
    private SeekBar seekBar;
    private TextView trackTimeValue;
    private TextView trackElapsedTimeValue;

    // Button's
    private Button buttonPlayerPlay;
    private Button buttonPlayerPause;
    private Button buttonPlayerNext;
    private Button buttonPlayerPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        eventBus.post(new PlayerUpdateEvent(Constants.PLAYER_STATUS_START));
    }

    private void setView() {
        trackTimeValue = (TextView) findViewById(R.id.activity_player_container_duration_track_time_value);
        trackElapsedTimeValue = (TextView) findViewById(R.id.activity_player_container_duration_track_elapsed_time_value);

        // Set SeekBar
        seekBar = (SeekBar) findViewById(R.id.activity_player_track_seekbar);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(this);

        // Set Button's
        buttonPlayerPlay = (Button) findViewById(R.id.activity_player_container_track_control_button_play);
        buttonPlayerPause = (Button) findViewById(R.id.activity_player_container_track_control_button_pause);
        buttonPlayerNext = (Button) findViewById(R.id.activity_player_container_track_control_button_next);
        buttonPlayerPrevious = (Button) findViewById(R.id.activity_player_container_track_control_button_previous);

        // Set Listener's
        buttonPlayerPlay.setOnClickListener(this);
        buttonPlayerPause.setOnClickListener(this);
        buttonPlayerNext.setOnClickListener(this);
        buttonPlayerPrevious.setOnClickListener(this);
    }

    @Subscribe
    public void onEvent(UpdateProgressBarEvent updateProgressBarEvent) {
        trackTimeValue.setText(TimeUtils.milliSecondsConverter(updateProgressBarEvent.getTrackTotalDuration()));
        trackElapsedTimeValue.setText(TimeUtils.milliSecondsConverter(updateProgressBarEvent.getTrackCurrentDuration()));
        seekBar.setProgress((int) updateProgressBarEvent.getTrackCurrentDuration());
        seekBar.setMax((int) updateProgressBarEvent.getTrackTotalDuration());
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.activity_player_container_track_control_button_pause):
                eventBus.post(new PausePlayerEvent());
                buttonPlayerPause.setVisibility(View.GONE);
                buttonPlayerPlay.setVisibility(View.VISIBLE);
                break;
            case (R.id.activity_player_container_track_control_button_play):
                eventBus.post(new PlayPausePlayerEvent());
                buttonPlayerPlay.setVisibility(View.GONE);
                buttonPlayerPause.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onStop() {
        // Event - Activity Player Close
        eventBus.post(new PlayerUpdateEvent(Constants.PLAYER_STATUS_STOP));
        eventBus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }
}
