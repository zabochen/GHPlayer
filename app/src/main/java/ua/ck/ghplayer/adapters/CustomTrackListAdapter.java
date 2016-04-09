package ua.ck.ghplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.models.Track;

public class CustomTrackListAdapter extends RecyclerView.Adapter<CustomTrackListAdapter.CustomListViewHolder> {
    private Context context;
    private ArrayList<Track> data;

    public CustomTrackListAdapter() {
        super();
    }

    public void setData(Context context, ArrayList<Track> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public CustomListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_track_list, parent, false);
        return new CustomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomListViewHolder holder, int position) {
        Track track = data.get(position);

        holder.title.setText(track.getTitle());
        holder.artist.setText(track.getArtist());
        holder.album.setText(track.getAlbum());

        Picasso.with(context)
                .load(track.getAlbumArt())
                .placeholder(R.drawable.bg_default_album_art)
                .error(R.drawable.bg_default_album_art)
                .into(holder.albumArt);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class CustomListViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView artist;
        private TextView album;
        private ImageView albumArt;

        public CustomListViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_track_list_title);
            artist = (TextView) itemView.findViewById(R.id.item_track_list_artist);
            album = (TextView) itemView.findViewById(R.id.item_track_list_album);
            albumArt = (ImageView) itemView.findViewById(R.id.item_track_list_album_art);
        }
    }
}
