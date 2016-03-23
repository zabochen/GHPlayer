package ua.ck.ghplayer.events;

public class StopMiniPlayerEvent {

    // Fields
    private long position;

    public StopMiniPlayerEvent(long id) {
        this.position = position;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
