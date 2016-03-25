package ua.ck.ghplayer.events;

public class StartMiniPlayerEvent {

    // Fields
    private int trackListId;
    private int position;

    public StartMiniPlayerEvent(int trackListId, int position) {
        this.trackListId = trackListId;
        this.position = position;
    }

    public int getTrackListId() {
        return trackListId;
    }

    public void setTrackListId(int trackListId) {
        this.trackListId = trackListId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
