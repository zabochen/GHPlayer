package ua.ck.ghplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.lists.TrackList;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.MyViewHolder> {

    private Context context;

    public TrackListAdapter() {
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
        holder.title.setText(TrackList.getInstance().getTrackList().get(position).getTitle());
        holder.artist.setText(TrackList.getInstance().getTrackList().get(position).getArtist());
        holder.album.setText(TrackList.getInstance().getTrackList().get(position).getAlbum());
        if (TrackList.getInstance().getTrackList().get(position).getAlbumArt() != null) {
            Picasso.with(context)
                    .load(TrackList.getInstance().getTrackList().get(position).getAlbumArt())
                    .into(holder.albumArt);
        } else {
            Picasso.with(context)
                    .load(R.drawable.album_cover_default)
                    .into(holder.albumArt);
        }
    }

    @Override
    public int getItemCount() {
        return TrackList.getInstance().getTrackList() != null ? TrackList.getInstance().getTrackList().size() : 0;
    }

    public void addToPlaylist(int position) {
        Toast.makeText(
                context,
                "Add: " + String.valueOf(TrackList.getInstance().getTrackList().get(position).getId()),
                Toast.LENGTH_LONG)
                .show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Views
        public TextView title;
        public TextView artist;
        public TextView album;
        public ImageView albumArt;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.item_track_list_title);
            this.artist = (TextView) itemView.findViewById(R.id.item_track_list_artist);
            this.album = (TextView) itemView.findViewById(R.id.item_track_list_album);
            this.albumArt = (ImageView) itemView.findViewById(R.id.item_track_list_album_art);
        }
    }
}
