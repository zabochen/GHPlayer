package ua.ck.ghplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import ua.ck.ghplayer.events.NotificationPlayerEvent;
import ua.ck.ghplayer.utils.Constants;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private EventBus eventBus = EventBus.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationRequest = intent.getAction();

        if (notificationRequest.equals(Constants.NOTIFICATION_PLAYER_BUTTON_PLAY)) {
            eventBus.post(new NotificationPlayerEvent(Constants.BROADCAST_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PLAY));
        } else if (notificationRequest.equals(Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE)) {
            eventBus.post(new NotificationPlayerEvent(Constants.BROADCAST_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PAUSE));
        } else if (notificationRequest.equals(Constants.NOTIFICATION_PLAYER_BUTTON_STOP)) {
            eventBus.post(new NotificationPlayerEvent(Constants.BROADCAST_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_STOP));
        } else if (notificationRequest.equals(Constants.NOTIFICATION_PLAYER_BUTTON_NEXT)) {
            eventBus.post(new NotificationPlayerEvent(Constants.BROADCAST_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_NEXT));
        } else if (notificationRequest.equals(Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS)) {
            eventBus.post(new NotificationPlayerEvent(Constants.BROADCAST_SENDER_ID, Constants.NOTIFICATION_PLAYER_BUTTON_PREVIOUS));
        }
    }
}
