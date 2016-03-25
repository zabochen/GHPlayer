package ua.ck.ghplayer.events;

public class NotificationPlayerEvent {

    private String buttonIntentFilter;

    public NotificationPlayerEvent(String buttonIntentFilter) {
        this.buttonIntentFilter = buttonIntentFilter;
    }

    public String getButtonIntentFilter() {
        return buttonIntentFilter;
    }

    public void setButtonIntentFilter(String buttonIntentFilter) {
        this.buttonIntentFilter = buttonIntentFilter;
    }
}
