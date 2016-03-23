package ua.ck.ghplayer.comparators;

import java.util.Comparator;

import ua.ck.ghplayer.models.Track;

public class TrackListComparator {

    public static class SortTitle implements Comparator<Track> {

        @Override
        public int compare(Track lhs, Track rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    }

    public static class SortArtist implements Comparator<Track> {

        @Override
        public int compare(Track lhs, Track rhs) {
            return lhs.getArtist().compareTo(rhs.getArtist());
        }
    }

    public static class SortAlbum implements Comparator<Track> {

        @Override
        public int compare(Track lhs, Track rhs) {
            return lhs.getAlbum().compareTo(rhs.getAlbum());
        }
    }

}
