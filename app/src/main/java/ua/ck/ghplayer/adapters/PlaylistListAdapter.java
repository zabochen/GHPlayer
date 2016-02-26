package ua.ck.ghplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.models.Playlist;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder>{
    private ArrayList data;

    public PlaylistListAdapter(ArrayList data) {
        super();
        this.data = data;
    }

    @Override
    public PlaylistListAdapter.PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_playlist_list, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistListAdapter.PlaylistViewHolder holder, int position) {
        Playlist playlist = (Playlist) data.get(position);
        //holder.cover.setImageBitmap();
        holder.name.setText(playlist.getName());
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }


    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public ImageView cover;
        public TextView name;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.item_playlist_list_cover);
            this.name = (TextView) itemView.findViewById(R.id.item_playlist_list_name);
        }
    }

}
