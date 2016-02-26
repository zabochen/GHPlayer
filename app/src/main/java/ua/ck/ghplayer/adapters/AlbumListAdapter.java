package ua.ck.ghplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.models.Album;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {
    private ArrayList data;

    public AlbumListAdapter(ArrayList data) {
        super();
        this.data = data;
    }

    @Override
    public AlbumListAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_album_list, parent, false);
        return new AlbumViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AlbumListAdapter.AlbumViewHolder holder, int position) {
        Album album = (Album) data.get(position);
        //holder.cover.setImageBitmap();
        holder.title.setText(album.getAlbum());
        holder.artist.setText(album.getArtist());
        holder.firstYear.setText(String.valueOf(album.getFirstYear()));
        holder.numberOfSongs.setText(String.valueOf(album.getNumberOfSongs()));

    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder{
        public ImageView cover;
        public TextView title;
        public TextView artist;
        public TextView firstYear;
        public TextView numberOfSongs;


        public AlbumViewHolder(View itemView) {
            super(itemView);
            this.cover=(ImageView) itemView.findViewById(R.id.item_album_list_cover);
            this.title=(TextView) itemView.findViewById(R.id.item_album_list_album);
            this.artist=(TextView) itemView.findViewById(R.id.item_album_list_artist);
            this.firstYear =(TextView) itemView.findViewById(R.id.item_album_list_first_year);
            this.numberOfSongs=(TextView) itemView.findViewById(R.id.item_album_list_number_of_songs);
        }


    }
}
