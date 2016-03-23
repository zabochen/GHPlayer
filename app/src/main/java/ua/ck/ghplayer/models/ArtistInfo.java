package ua.ck.ghplayer.models;


public class ArtistInfo {

    private String summary;
    private String artistArtUrl;

    public String getSummary() {
        return summary;
    }

    public String getArtistArtUrl() {
        return artistArtUrl;
    }

    public ArtistInfo(String summary, String artistArtUrl) {
        super();
        this.summary = summary;
        this.artistArtUrl = artistArtUrl;

    }
}
