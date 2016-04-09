package ua.ck.ghplayer.events;

public class MiniPlayerButtonEvent {

    private int senderId;
    private int clickButton;


    public MiniPlayerButtonEvent(int senderId, int clickButton) {
        this.senderId = senderId;
        this.clickButton = clickButton;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getClickButton() {
        return clickButton;
    }

    public void setClickButton(int clickButton) {
        this.clickButton = clickButton;
    }
}
