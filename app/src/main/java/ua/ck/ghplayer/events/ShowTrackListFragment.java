package ua.ck.ghplayer.events;

public class ShowTrackListFragment {

    // Fields
    private int showTrackList;          // Which fragment to show. Use constants: SHOW_GENRE_TRACK_LIST ...
    private long id;                    // This is ID selected (touched in list): Genre ...

    public ShowTrackListFragment(int showTrackList, long id) {
        this.showTrackList = showTrackList;
        this.id = id;
    }

    public int getShowTrackList() {
        return showTrackList;
    }

    public void setShowTrackList(int showTrackList) {
        this.showTrackList = showTrackList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
