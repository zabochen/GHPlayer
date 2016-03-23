package ua.ck.ghplayer.interfaces;

public interface ItemClickFragmentSetter {
    void onAlbumListItemClick(String album, int albumId);

    void onArtistListItemClick(int artistId);

    //void onGenreListItemClick();

    void onPlaylistListItemClick(int playlistId);
}
