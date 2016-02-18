package ua.ck.ghplayer.models;

public class Track {

    // Fields
    private String title;       // Type: TEXT, Constant Value: "title"
    private String artist;      // Type: TEXT, Constant Value: "artist"
    private String album;       // Type: TEXT, Constant Value: "album"
    private long duration;      // Type: INTEGER (long), Constant Value: "duration"

    // Constants
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String DURATION = "duration";

    public Track(String title, String artist, String album, long duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
