package ua.ck.ghplayer.events;

public class StartMiniPlayerEvent {

    // Fields
    private int position;

    public StartMiniPlayerEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
