package ua.ck.ghplayer.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.activities.MainActivity;
import ua.ck.ghplayer.utils.Constants;

public class NotificationPlayer {

    private static final int NOTIFICATION_ID = 8;
    private Context context;

    // Instance
    private EventBus eventBus = EventBus.getDefault();

    public NotificationPlayer(Context context) {
        this.context = context;
    }

    public void startNotificationPlayer(String pressButton) {

        // Set Notification Layout
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

        Intent intentButtonNext = new Intent(Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE);
        PendingIntent pendingIntentButtonNext = PendingIntent.getBroadcast(
                context, 0, intentButtonNext, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_player_button_next, pendingIntentButtonNext);

        // Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntentClickNotification)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Big Notification
        Notification notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.bigContentView = remoteViews;
        notification.bigContentView.setTextViewText(R.id.notification_player_title, "Title");

        // Notify
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
