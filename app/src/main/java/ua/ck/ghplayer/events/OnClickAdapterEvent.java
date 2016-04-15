package ua.ck.ghplayer.events;

public class OnClickAdapterEvent {

    private int trackListId;
    private int trackPosition;

    public OnClickAdapterEvent(int trackListId, int trackPosition) {
        this.trackListId = trackListId;
        this.trackPosition = trackPosition;
    }

    public int getTrackListId() {
        return trackListId;
    }

    public void setTrackListId(int trackListId) {
        this.trackListId = trackListId;
    }

    public int getTrackPosition() {
        return trackPosition;
    }

    public void setTrackPosition(int trackPosition) {
        this.trackPosition = trackPosition;
    }
}
