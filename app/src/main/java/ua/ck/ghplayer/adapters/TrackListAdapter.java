package ua.ck.ghplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.lists.TrackList;
import ua.ck.ghplayer.models.Track;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.MyViewHolder> {

    private ArrayList<Track> trackList = TrackList.getInstance().getTrackList();

    public TrackListAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_track_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(trackList.get(position).getTitle());
        holder.artist.setText(trackList.get(position).getArtist());
        holder.album.setText(trackList.get(position).getAlbum());
    }

    @Override
    public int getItemCount() {
        return this.trackList != null ? trackList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Views
        public TextView title;
        public TextView artist;
        public TextView album;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.item_track_list_title);
            this.artist = (TextView) itemView.findViewById(R.id.item_track_list_artist);
            this.album = (TextView) itemView.findViewById(R.id.item_track_list_album);
        }
    }

}
