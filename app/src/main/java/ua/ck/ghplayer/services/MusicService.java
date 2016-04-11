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
import java.util.ArrayList;

import ua.ck.ghplayer.events.MiniPlayerButtonEvent;
import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.events.PlayerButtonEvent;
import ua.ck.ghplayer.events.PlayerUpdateEvent;
import ua.ck.ghplayer.events.TouchProgressBarEvent;
import ua.ck.ghplayer.events.UpdateProgressBarEvent;
import ua.ck.ghplayer.events.UpdateTrackContentEvent;
import ua.ck.ghplayer.lists.AlbumTrackList;
import ua.ck.ghplayer.lists.ArtistTrackList;
import ua.ck.ghplayer.lists.FavoriteTrackList;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.notification.NotificationPlayer;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.ServiceUtils;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private Uri trackUri;
    private MediaPlayer mediaPlayer;
    private EventBus eventBus = EventBus.getDefault();
    private Handler handlerUpdateProgressBar = new Handler();

    // TrackList & Track Position
    private int trackListId;           // What "TrackList" we work
    private int trackPosition;         // Selected "Track"

    // Notification Player
    private NotificationPlayer notificationPlayer;

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
        trackListId = intent.getIntExtra(Constants.TRACK_LIST_ID_KEY, 0);
        trackPosition = intent.getIntExtra(Constants.MINI_PLAYER_TRACK_POSITION_KEY, 0);

        // Track URI
        updateTrackUri();

        // Play Track
        playMediaPlayer();

        // Start Notification Player
        if (ServiceUtils.musicServiceRunning(getApplicationContext(), MusicService.class)) {
            eventBus.post(new NotificationPlayerEvent(Constants.MUSIC_SERVICE_SENDER_ID, Constants.NOTIFICATION_PLAYER_START));
        }

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
                        TrackList.getInstance().getTrackList().get(trackPosition).getId());
                break;
            case (Constants.ALBUM_TRACK_LIST_ID):
                trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getId());
                break;
            case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getId());
                break;
            case (Constants.GENRE_TRACK_LIST_ID):
                trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getId());
                break;
            case (Constants.FAVORITE_TRACK_LIST_ID):
                trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getId());
                break;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Media Player Method's
    // ---------------------------------------------------------------------------------------------

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

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            handlerUpdateProgressBar.removeCallbacks(updateTimeTask);
            mediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        eventBus.post(new NotificationPlayerEvent(Constants.MUSIC_SERVICE_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_NEXT));
    }

    private void pauseMediaPlayer() {
        mediaPlayer.pause();
    }

    private void playPauseMediaPlayer() {
        mediaPlayer.start();
    }

    public void nextTrack(ArrayList trackList) {
        if (trackPosition < (trackList.size() - 1)) {
            trackPosition += 1;
            updateTrackUri();
            releaseMediaPlayer();
            playMediaPlayer();
            updateProgressBar();
        } else {
            trackPosition = 0;
            updateTrackUri();
            releaseMediaPlayer();
            playMediaPlayer();
            updateProgressBar();
        }
    }

    public void previousTrack(ArrayList trackList) {
        if (trackPosition > 0) {
            trackPosition -= 1;
            updateTrackUri();
            releaseMediaPlayer();
            playMediaPlayer();
            updateProgressBar();
        } else {
            trackPosition = (trackList.size() - 1);
            updateTrackUri();
            releaseMediaPlayer();
            playMediaPlayer();
            updateProgressBar();
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

    // ---------------------------------------------------------------------------------------------
    // Notification Player
    // ---------------------------------------------------------------------------------------------

    @Subscribe
    public void onNotificationPlayerEvent(NotificationPlayerEvent event) {

        notificationPlayer = new NotificationPlayer(getApplicationContext());

        switch (event.getButtonIntentFilter()) {

            // -------------------------------------------------------------------------------------
            // Start Notification Player
            // -------------------------------------------------------------------------------------

            case (Constants.NOTIFICATION_PLAYER_START):
                switch (trackListId) {
                    case (Constants.MAIN_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_EMPTY,
                                TrackList.getInstance().getTrackList().get(trackPosition).getTitle(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getArtist(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ALBUM_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_EMPTY,
                                AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getTitle(),
                                AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getArtist(),
                                AlbumTrackList.getInstance().getSaveAlbumTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_EMPTY,
                                ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getTitle(),
                                ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getArtist(),
                                ArtistTrackList.getInstance().getSaveArtistTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.GENRE_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_EMPTY,
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.FAVORITE_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_EMPTY,
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getTitle(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getArtist(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                }
                break;

            // -------------------------------------------------------------------------------------
            // Play Button Notification Player
            // -------------------------------------------------------------------------------------

            case (Constants.NOTIFICATION_PLAYER_BUTTON_PLAY):
                switch (trackListId) {
                    case (Constants.MAIN_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PLAY,
                                TrackList.getInstance().getTrackList().get(trackPosition).getTitle(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getArtist(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ALBUM_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PLAY,
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getTitle(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getArtist(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PLAY,
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getTitle(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getArtist(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.GENRE_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PLAY,
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.FAVORITE_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PLAY,
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getTitle(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getArtist(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                }

                switch (event.getSenderId()) {
                    case (Constants.BROADCAST_SENDER_ID):
                        playPauseMediaPlayer();
                        eventBus.post(new MiniPlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.MINI_PLAYER_BUTTON_PLAY));
                        eventBus.post(new PlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.PLAYER_BUTTON_PLAY));
                        break;
                    case (Constants.MINI_PLAYER_SENDER_ID):
                        playPauseMediaPlayer();
                        break;
                    case (Constants.PLAYER_SENDER_ID):
                        playPauseMediaPlayer();
                        break;
                    case (Constants.MAIN_ACTIVITY_SENDER_ID):
                        // Update Notification Player - Set active button Pause
                        break;
                }

                break;

            // -------------------------------------------------------------------------------------
            // Pause Button Notification Player
            // -------------------------------------------------------------------------------------

            case (Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE):
                switch (trackListId) {
                    case (Constants.MAIN_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE,
                                TrackList.getInstance().getTrackList().get(trackPosition).getTitle(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getArtist(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ALBUM_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE,
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getTitle(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getArtist(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE,
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getTitle(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getArtist(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.GENRE_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE,
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.FAVORITE_TRACK_LIST_ID):
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE,
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getTitle(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getArtist(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                }

                // Pause Track
                pauseMediaPlayer();

                switch (event.getSenderId()) {
                    case (Constants.BROADCAST_SENDER_ID):
                        eventBus.post(new MiniPlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.MINI_PLAYER_BUTTON_PAUSE));
                        eventBus.post(new PlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.PLAYER_BUTTON_PAUSE));
                        break;
                }

                break;

            // -------------------------------------------------------------------------------------
            // Stop Button Notification Player
            // -------------------------------------------------------------------------------------

            case (Constants.NOTIFICATION_PLAYER_BUTTON_STOP):
                mediaPlayer.stop();
                notificationPlayer.stopNotificationPlayer();
                stopMusicService();

                switch (event.getSenderId()) {
                    case (Constants.BROADCAST_SENDER_ID):
                        eventBus.post(new MiniPlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.MINI_PLAYER_BUTTON_STOP));
                        break;
                }
                break;

            // -------------------------------------------------------------------------------------
            // Next Button Notification Player
            // -------------------------------------------------------------------------------------

            case (Constants.NOTIFICATION_PLAYER_BUTTON_NEXT):
                switch (trackListId) {
                    case (Constants.MAIN_TRACK_LIST_ID):
                        nextTrack(TrackList.getInstance().getTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_NEXT,
                                TrackList.getInstance().getTrackList().get(trackPosition).getTitle(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getArtist(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ALBUM_TRACK_LIST_ID):
                        nextTrack(AlbumTrackList.getInstance().getAlbumTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_NEXT,
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getTitle(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getArtist(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                        nextTrack(ArtistTrackList.getInstance().getArtistTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_NEXT,
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getTitle(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getArtist(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.GENRE_TRACK_LIST_ID):
                        nextTrack(GenreTrackList.getInstance().getGenreTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_NEXT,
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.FAVORITE_TRACK_LIST_ID):
                        nextTrack(FavoriteTrackList.getInstance().getFavoriteTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_NEXT,
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getTitle(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getArtist(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                }

                // Update track content
                eventBus.post(new UpdateTrackContentEvent(trackListId, trackPosition));

                // Set active button Pause MiniPlayer
                eventBus.post(new MiniPlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.MINI_PLAYER_BUTTON_NEXT));

                // Set active button Pause Player
                eventBus.post(new PlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.PLAYER_BUTTON_NEXT));

                break;

            // ---------------------------------------------------------------------------------------------
            // Previous Button Notification Player
            // ---------------------------------------------------------------------------------------------

            case (Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS):
                switch (trackListId) {
                    case (Constants.MAIN_TRACK_LIST_ID):
                        previousTrack(TrackList.getInstance().getTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS,
                                TrackList.getInstance().getTrackList().get(trackPosition).getTitle(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getArtist(),
                                TrackList.getInstance().getTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ALBUM_TRACK_LIST_ID):
                        previousTrack(AlbumTrackList.getInstance().getAlbumTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS,
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getTitle(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getArtist(),
                                AlbumTrackList.getInstance().getAlbumTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.ARTIST_ALBUM_TRACK_LIST_ID):
                        previousTrack(ArtistTrackList.getInstance().getArtistTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS,
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getTitle(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getArtist(),
                                ArtistTrackList.getInstance().getArtistTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.GENRE_TRACK_LIST_ID):
                        previousTrack(GenreTrackList.getInstance().getSaveGenreTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS,
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getTitle(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getArtist(),
                                GenreTrackList.getInstance().getSaveGenreTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                    case (Constants.FAVORITE_TRACK_LIST_ID):
                        previousTrack(FavoriteTrackList.getInstance().getFavoriteTrackList());
                        notificationPlayer.startNotificationPlayer(
                                Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS,
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getTitle(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getArtist(),
                                FavoriteTrackList.getInstance().getSaveFavoriteTrackList().get(trackPosition).getAlbumId()
                        );
                        break;
                }

                // Update track content
                eventBus.post(new UpdateTrackContentEvent(trackListId, trackPosition));

                // Set active button Pause MiniPlayer
                eventBus.post(new MiniPlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.MINI_PLAYER_BUTTON_PREVIOUS));

                // Set active button Pause Player
                eventBus.post(new PlayerButtonEvent(Constants.NOTIFICATION_PLAYER_SENDER_ID, Constants.PLAYER_BUTTON_PREVIOUS));

                break;
        }
    }

    public void stopMusicService() {
        Intent musicServiceStopIntent = new Intent(getApplicationContext(), MusicService.class);
        stopService(musicServiceStopIntent);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

}
