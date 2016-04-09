package ua.ck.ghplayer.events;

public class NotificationPlayerEvent {

    private int senderId;
    private String buttonIntentFilter;

    public NotificationPlayerEvent(int senderId, String buttonIntentFilter) {
        this.senderId = senderId;
        this.buttonIntentFilter = buttonIntentFilter;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getButtonIntentFilter() {
        return buttonIntentFilter;
    }

    public void setButtonIntentFilter(String buttonIntentFilter) {
        this.buttonIntentFilter = buttonIntentFilter;
    }
}
