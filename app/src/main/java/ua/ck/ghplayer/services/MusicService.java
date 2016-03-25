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
import android.support.v4.app.NotificationManagerCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.events.PausePlayerEvent;
import ua.ck.ghplayer.events.PlayMiniPlayerEvent;
import ua.ck.ghplayer.events.PlayPausePlayerEvent;
import ua.ck.ghplayer.events.PlayerUpdateEvent;
import ua.ck.ghplayer.events.StopMiniPlayerEvent;
import ua.ck.ghplayer.events.TouchProgressBarEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.notification.NotificationPlayer;
import ua.ck.ghplayer.utils.Constants;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private EventBus eventBus = EventBus.getDefault();
    private MediaPlayer mediaPlayer;
    private Uri trackUri;
    private Handler handlerUpdateProgressBar = new Handler();

    private int trackListId;                            // What "TrackList" we work
    private static int miniPlayerTrackPosition;         // Selected "Track"

    // Update ProgressBar
    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            eventBus.post(new UpdateProgressBarEvent(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition()));
            handlerUpdateProgressBar.postDelayed(this, Constants.PLAYER_PROGRESS_BAR_UPDATE_TIME_INTERVAL);
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
        miniPlayerTrackPosition = intent.getIntExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, 0);
        trackListId = intent.getIntExtra(Constants.TRACK_LISTS_ID_KEY, 0);

        // Track URI
        updateTrackUri();

        playMediaPlayer();
        return START_NOT_STICKY;
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

    // Create or Update Track URI

    public void updateTrackUri() {
        switch (trackListId) {
            case (Constants.MAIN_TRACK_LIST_ID):
                trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        TrackList.getInstance().getTrackList().get(miniPlayerTrackPosition).getId());
                break;
            case (Constants.ALBUM_TRACK_LIST_ID):
                break;
            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                break;
            case (Constants.GENRE_TRACK_LIST_ID):
                trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        GenreTrackList.getInstance().getGenreTrackList().get(miniPlayerTrackPosition).getId());
                break;
        }
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
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.prepare();
            mediaPlayer.start();
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

    public void nextTrack(ArrayList trackList) {
        if (miniPlayerTrackPosition < (trackList.size() - 1)) {
            miniPlayerTrackPosition += 1;
            updateTrackUri();
            handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
            playMediaPlayer();
            updateProgressBar();
        } else {
            miniPlayerTrackPosition = 0;
            updateTrackUri();
            handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
            playMediaPlayer();
            updateProgressBar();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (trackListId) {
            case (Constants.MAIN_TRACK_LIST_ID):
                nextTrack(TrackList.getInstance().getTrackList());
                break;
            case (Constants.GENRE_TRACK_LIST_ID):
                nextTrack(GenreTrackList.getInstance().getGenreTrackList());
                break;
        }
    }

    private void updateProgressBar() {
        handlerUpdateProgressBar.postDelayed(updateTimeTask, Constants.PLAYER_PROGRESS_BAR_UPDATE_TIME_INTERVAL);
    }

    @Subscribe
    public void onUpdatePlayerEvent(PlayerUpdateEvent event) {
        switch (event.getPlayerStatus()) {
            case (Constants.PLAYER_STATUS_START):
                updateProgressBar();
                break;
            case (Constants.PLAYER_STATUS_STOP):
                handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
                break;
        }
    }

    @Subscribe
    public void onTouchProgressBarEvent(TouchProgressBarEvent touchProgressBarEvent) {
        if (mediaPlayer != null) {
            handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
            mediaPlayer.seekTo(touchProgressBarEvent.getTrackCurrentPosition());
            updateProgressBar();
        }
    }

    @Subscribe
    public void onPlayMiniPlayerEvent(PlayMiniPlayerEvent event) {
        playMediaPlayer();
    }

    @Subscribe
    public void onStopMiniPlayerEvent(StopMiniPlayerEvent event) {
        mediaPlayer.stop();
    }

    @Subscribe
    public void onPausePlayerEvent(PausePlayerEvent event) {
        pauseMediaPlayer();
    }

    @Subscribe
    public void onPlayPausePlayerEvent(PlayPausePlayerEvent event) {
        playPauseMediaPlayer();
    }

    // ---------------------------------------------------------------------------------------------
    // Notification
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onNotificationPlayerEvent(NotificationPlayerEvent event) {

        NotificationPlayer notificationPlayer = new NotificationPlayer(getApplicationContext());

        switch (event.getButtonIntentFilter()) {
            case (Constants.NOTIFICATION_PLAYER_START):
                notificationPlayer.startNotificationPlayer("");
                break;

            case (Constants.NOTIFICATION_PLAYER_BUTTON_PLAY):
                playPauseMediaPlayer();
                notificationPlayer.startNotificationPlayer(Constants.NOTIFICATION_PLAYER_BUTTON_PLAY);
                break;

            case (Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE):
                pauseMediaPlayer();
                notificationPlayer.startNotificationPlayer(Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE);
                break;

            case (Constants.NOTIFICATION_PLAYER_BUTTON_STOP):
                mediaPlayer.stop();
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.cancel(8);
                getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                break;

        }
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }


}
