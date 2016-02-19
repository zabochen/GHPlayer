package ua.ck.ghplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.ck.ghplayer.R;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {

    @Override
    public AlbumListAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_album_list, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumListAdapter.AlbumViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder{
        public ImageView cover;
        public TextView title;
        public TextView artist;
        public TextView year;
        public TextView numberOfSongs;


        public AlbumViewHolder(View itemView) {
            super(itemView);
            this.cover=(ImageView) itemView.findViewById(R.id.item_album_list_cover);
            this.title=(TextView) itemView.findViewById(R.id.item_album_list_title);
            this.artist=(TextView) itemView.findViewById(R.id.item_album_list_artist);
            this.year=(TextView) itemView.findViewById(R.id.item_album_list_year);
            this.numberOfSongs=(TextView) itemView.findViewById(R.id.item_album_list_number_of_songs);
        }


    }
}
