package ua.ck.ghplayer.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import ua.ck.ghplayer.events.PausePlayerEvent;
import ua.ck.ghplayer.events.PlayMiniPlayerEvent;
import ua.ck.ghplayer.events.PlayPausePlayerEvent;
import ua.ck.ghplayer.events.StopMiniPlayerEvent;
import ua.ck.ghplayer.events.TouchProgressBarEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.lists.TrackList;

public class MusicService extends Service {

    private EventBus eventBus = EventBus.getDefault();
    private MediaPlayer mediaPlayer;
    private Uri trackUri;
    private Handler handlerUpdateProgressBar = new Handler();

    // Constants
    private static final String KEY_NAVIGATION_VIEW_ITEM_SELECTED = "NavigationViewItemSelected";
    private static final String KEY_MINI_PLAYER_TRACK_POSITION = "MiniPlayerTrackPosition";

    // Values
    private static int miniPlayerTrackPosition;
    private static int navigationViewItemSelected;

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            eventBus.post(new UpdateProgressBarEvent(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition()));
            handlerUpdateProgressBar.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus.register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get MusicPlayer Intent
        navigationViewItemSelected = intent.getIntExtra(KEY_NAVIGATION_VIEW_ITEM_SELECTED, 0);
        miniPlayerTrackPosition = intent.getIntExtra(KEY_MINI_PLAYER_TRACK_POSITION, 0);

        // URI
        trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                TrackList.getInstance().getTrackList().get(miniPlayerTrackPosition).getId());

        playMediaPlayer();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
            mediaPlayer = null;
        }
    }

    private void playMediaPlayer() {
        releaseMediaPlayer();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            updateProgressBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseMediaPlayer() {
        mediaPlayer.pause();
    }

    private void playPauseMediaPlayer() {
        mediaPlayer.start();
    }

    private void updateProgressBar() {
        handlerUpdateProgressBar.postDelayed(updateTimeTask, 1000);
    }

    @Subscribe
    public void onEvent(TouchProgressBarEvent touchProgressBarEvent) {
        if (mediaPlayer != null) {
            handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
            mediaPlayer.seekTo(touchProgressBarEvent.getTrackCurrentPosition());
            updateProgressBar();
        }
    }

    @Subscribe
    public void onEvent(PlayMiniPlayerEvent event) {
        playMediaPlayer();
    }

    @Subscribe
    public void onEvent(StopMiniPlayerEvent event) {
        mediaPlayer.stop();
    }

    @Subscribe
    public void onEvent(PausePlayerEvent event) {
        pauseMediaPlayer();
    }

    @Subscribe
    public void onEvent(PlayPausePlayerEvent event) {
        playPauseMediaPlayer();
    }
}
