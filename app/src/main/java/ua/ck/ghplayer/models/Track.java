package ua.ck.ghplayer.models;

public class Track {

    // Fields
    private long id;            // Type: INTEGER (long), Constants Value: "_id"
    private String title;       // Type: TEXT, Constants Value: "title"
    private String artist;      // Type: TEXT, Constants Value: "artist"
    private String album;       // Type: TEXT, Constants Value: "album"
    private long duration;      // Type: INTEGER (long), Constants Value: "duration"

    // Constants
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String DURATION = "duration";

    public Track(long id, String title, String artist, String album, long duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
