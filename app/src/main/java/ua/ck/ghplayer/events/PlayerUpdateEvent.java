package ua.ck.ghplayer.events;

public class PlayerUpdateEvent {

    private int playerStatus;

    public PlayerUpdateEvent(int playerStatus) {
        this.playerStatus = playerStatus;
    }

    public int getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(int playerStatus) {
        this.playerStatus = playerStatus;
    }
}
