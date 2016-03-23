package ua.ck.ghplayer.events;

public class UpdateProgressBarEvent {

    // Fields
    private long trackTotalDuration;
    private long trackCurrentDuration;

    public UpdateProgressBarEvent(long trackTotalDuration, long trackCurrentDuration) {
        this.trackTotalDuration = trackTotalDuration;
        this.trackCurrentDuration = trackCurrentDuration;
    }

    public long getTrackTotalDuration() {
        return trackTotalDuration;
    }

    public void setTrackTotalDuration(long trackTotalDuration) {
        this.trackTotalDuration = trackTotalDuration;
    }

    public long getTrackCurrentDuration() {
        return trackCurrentDuration;
    }

    public void setTrackCurrentDuration(long trackCurrentDuration) {
        this.trackCurrentDuration = trackCurrentDuration;
    }
}
