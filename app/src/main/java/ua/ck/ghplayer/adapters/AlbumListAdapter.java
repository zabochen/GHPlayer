package ua.ck.ghplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.activities.CustomTrackListActivity;
import ua.ck.ghplayer.events.ShowTrackListActivity;
import ua.ck.ghplayer.models.Album;
import ua.ck.ghplayer.utils.Constants;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {
    private Context context;
    private ArrayList<Album> data;

    public AlbumListAdapter() {
        super();
    }

    public void setData(Context context, ArrayList<Album> data) {
        this.context = context;
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
        Album album = data.get(position);
        Picasso.with(context)
                .load(album.getAlbumCoverUri())
                .placeholder(R.drawable.album_cover_default)
                .into(holder.cover);
        String firstYear = album.getFirstYear() > 0 ? String.valueOf(album.getFirstYear()) : "";
        String title = new StringBuffer()
                .append(album.getAlbum())
                .append(" - ")
                .append(firstYear)
                .toString();
        holder.title.setText(title);
        holder.artist.setText(album.getArtist());
        holder.numberOfSongs.setText(String.valueOf(album.getNumberOfSongs()));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView cover;
        public TextView title;
        public TextView artist;
        public TextView numberOfSongs;


        public AlbumViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.item_album_list_cover);
            this.title = (TextView) itemView.findViewById(R.id.item_album_list_album);
            this.artist = (TextView) itemView.findViewById(R.id.item_album_list_artist);
            this.numberOfSongs = (TextView) itemView.findViewById(R.id.item_album_list_number_of_songs);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            EventBus.getDefault().post(new ShowTrackListActivity(Constants.ALBUM_TRACK_LIST_ID, position));

/*            Intent intent = new Intent(context, CustomTrackListActivity.class);
            intent.putExtra("CHOICE_MODE", "ALBUM");
            intent.putExtra("ALBUM_NAME", data.get(position).getAlbum());
            intent.putExtra("ALBUM_ID", data.get(position).getId());
            context.startActivity(intent);
*/
        }
    }
}
