package ua.ck.ghplayer.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ArtistInfo extends RealmObject {
    @PrimaryKey
    @Index
    @Required
    private String artistName;
    private String content;
    private String summary;
    private String artistArtUrl;

    public ArtistInfo() {
        super();
    }

    public ArtistInfo(String artistName, String summary, String content, String artistArtUrl) {
        super();
        this.artistName = artistName;
        this.content = content;
        this.summary = summary;
        this.artistArtUrl = artistArtUrl;
    }

    public String getArtistArtUrl() {
        return artistArtUrl;
    }

    public void setArtistArtUrl(String artistArtUrl) {
        this.artistArtUrl = artistArtUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
