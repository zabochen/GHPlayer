package ua.ck.ghplayer.events;

public class ShowTrackListActivity {

    // Field's
    private int trackListId;            // Which trackList to show. Use constants: MAIN_TRACK_LIST_ID, ALBUM_TRACK_LIST_ID ...
    private int position;               // This is Position selected item in list RecyclerView: Album, Genre ...

    public ShowTrackListActivity(int trackListId, int position) {
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
