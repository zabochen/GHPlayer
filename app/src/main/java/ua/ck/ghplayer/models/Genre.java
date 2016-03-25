package ua.ck.ghplayer.models;

public class Genre {

    // Fields
    private long id;            // Type: INTEGER (long), Constant Value: "_id"
    private String name;        // Type: TEXT, Constant Value: "name"

    // Constants Genre
    public static final String ID = "_id";
    public static final String NAME = "name";

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
