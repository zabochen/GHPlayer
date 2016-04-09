package ua.ck.ghplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.activities.CustomTrackListActivity;
import ua.ck.ghplayer.models.Playlist;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder> {
    private Context context;
    private ArrayList<Playlist> data;

    public PlaylistListAdapter() {
        super();
    }

    public void setData(Context context, ArrayList<Playlist> data) {
        this.context = context;
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
        Playlist playlist = data.get(position);
        holder.name.setText(playlist.getName());
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.item_playlist_list_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            Intent intent = new Intent(context, CustomTrackListActivity.class);
            intent.putExtra("CHOICE_MODE", "PLAYLIST");
            intent.putExtra("PLAYLIST_NAME", data.get(position).getName());
            intent.putExtra("PLAYLIST_ID", data.get(position).getId());
            context.startActivity(intent);
        }
    }

}
