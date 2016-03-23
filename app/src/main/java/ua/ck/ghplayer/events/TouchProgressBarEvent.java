package ua.ck.ghplayer.events;

public class TouchProgressBarEvent {

    // Field's
    private int trackCurrentPosition;

    public TouchProgressBarEvent(int trackCurrentPosition) {
        this.trackCurrentPosition = trackCurrentPosition;
    }

    public int getTrackCurrentPosition() {
        return trackCurrentPosition;
    }

    public void setTrackCurrentPosition(int trackCurrentPosition) {
        this.trackCurrentPosition = trackCurrentPosition;
    }
}
