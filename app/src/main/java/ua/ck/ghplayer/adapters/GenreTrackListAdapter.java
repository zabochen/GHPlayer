package ua.ck.ghplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.lists.GenreList;
import ua.ck.ghplayer.lists.GenreTrackList;
import ua.ck.ghplayer.lists.TrackList;

public class GenreTrackListAdapter extends RecyclerView.Adapter<GenreTrackListAdapter.MyViewHolder> {

    private Context context;

    public GenreTrackListAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_track_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(GenreTrackList.getInstance().getGenreTrackList().get(position).getTitle());
        holder.artist.setText(GenreTrackList.getInstance().getGenreTrackList().get(position).getArtist());
        holder.album.setText(GenreTrackList.getInstance().getGenreTrackList().get(position).getAlbum());

        if (GenreTrackList.getInstance().getGenreTrackList().get(position).getAlbumArt() != null) {
            Picasso.with(context)
                    .load(GenreTrackList.getInstance().getGenreTrackList().get(position).getAlbumArt())
                    .into(holder.albumArt);
        } else {
            Picasso.with(context)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.albumArt);
        }
    }

    @Override
    public int getItemCount() {
        return GenreTrackList.getInstance().getGenreTrackList() != null ? GenreTrackList.getInstance().getGenreTrackList().size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Views
        private TextView title;
        private TextView artist;
        private TextView album;
        private ImageView albumArt;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.item_track_list_title);
            this.artist = (TextView) itemView.findViewById(R.id.item_track_list_artist);
            this.album = (TextView) itemView.findViewById(R.id.item_track_list_album);
            this.albumArt = (ImageView) itemView.findViewById(R.id.item_track_list_album_art);
        }
    }
}
