package ua.ck.ghplayer.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.RemoteViews;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.activities.MainActivity;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.ImageUtils;

public class NotificationPlayer {

    private Context context;

    public NotificationPlayer(Context context) {
        this.context = context;
    }

    public void startNotificationPlayer(String pressButton, String title, String artist, long albumId) {

        // Set "Big" Notification Layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_player);

        if (pressButton.equals(Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE)) {
            remoteViews.setViewVisibility(R.id.notification_player_button_pause, View.GONE);
            remoteViews.setViewVisibility(R.id.notification_player_button_play, View.VISIBLE);
        } else if (pressButton.equals(Constants.NOTIFICATION_PLAYER_BUTTON_PLAY)) {
            remoteViews.setViewVisibility(R.id.notification_player_button_pause, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.notification_player_button_play, View.GONE);
        }

        // Click Notification - Start created activity
        Intent intentClickNotification = new Intent(context, MainActivity.class);
        intentClickNotification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntentClickNotification = PendingIntent.getActivity(
                context, 0, intentClickNotification, PendingIntent.FLAG_CANCEL_CURRENT);

        // Button Listener's

        Intent intentButtonPrevious = new Intent(Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS);
        PendingIntent pendingIntentButtonPrevious = PendingIntent.getBroadcast(
                context, 0, intentButtonPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_player_button_previous, pendingIntentButtonPrevious);

        Intent intentButtonPlay = new Intent(Constants.NOTIFICATION_PLAYER_BUTTON_PLAY);
        PendingIntent pendingIntentButtonPlay = PendingIntent.getBroadcast(
                context, 0, intentButtonPlay, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_player_button_play, pendingIntentButtonPlay);

        Intent intentButtonPause = new Intent(Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE);
        PendingIntent pendingIntentButtonPause = PendingIntent.getBroadcast(
                context, 0, intentButtonPause, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_player_button_pause, pendingIntentButtonPause);

        Intent intentButtonStop = new Intent(Constants.NOTIFICATION_PLAYER_BUTTON_STOP);
        PendingIntent pendingIntentButtonStop = PendingIntent.getBroadcast(
                context, 0, intentButtonStop, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_player_button_stop, pendingIntentButtonStop);

        Intent intentButtonNext = new Intent(Constants.NOTIFICATION_PLAYER_BUTTON_NEXT);
        PendingIntent pendingIntentButtonNext = PendingIntent.getBroadcast(
                context, 0, intentButtonNext, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_player_button_next, pendingIntentButtonNext);

        // Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntentClickNotification)
                .setSmallIcon(R.drawable.notification_player_icon)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Big Notification
        Notification notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.bigContentView = remoteViews;
        notification.bigContentView.setTextViewText(R.id.notification_player_title, title);
        notification.bigContentView.setTextViewText(R.id.notification_player_artist, artist);
        if(ImageUtils.getBitmapAlbumart(context, albumId) != null){
            notification.bigContentView.setImageViewBitmap(R.id.notification_player_album_art, ImageUtils.getBitmapAlbumart(context, albumId));
        }

        // Notify
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Constants.NOTIFICATION_PLAYER_ID, notification);
    }

    public void stopNotificationPlayer() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(Constants.NOTIFICATION_PLAYER_ID);
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
}
