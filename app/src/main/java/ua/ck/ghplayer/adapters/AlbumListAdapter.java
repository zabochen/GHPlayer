package ua.ck.ghplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.activities.MainActivity;
import ua.ck.ghplayer.events.ShowTrackListActivity;
import ua.ck.ghplayer.models.Album;
import ua.ck.ghplayer.utils.Constants;
import ua.ck.ghplayer.utils.PlaylistUtils;

public class AlbumListAdapter extends RecyclerSwipeAdapter<AlbumListAdapter.AlbumViewHolder> {
    private Context context;
    private ArrayList<Album> data;
    private EventBus eventBus = EventBus.getDefault();

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
    public void onBindViewHolder(AlbumListAdapter.AlbumViewHolder holder, final int position) {
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


        // Swipe Layout
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.item_album_list_bottom_view));

        // Main Surface Click
        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sending the current track position in the MiniPlayer
                if(context.getClass() == MainActivity.class) {
                    EventBus.getDefault().post(new ShowTrackListActivity(Constants.ALBUM_TRACK_LIST_ID, position));
                }
            }
        });

        // Button Cancel Click
        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.closeAllItems();
            }
        });

        // Button Favorite Click
        holder.buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistUtils.AddAlbumTracksToFavorite(context, data.get(position).getId());
                mItemManger.closeAllItems();
            }
        });

        // Swipe Item Manger
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.item_album_list_swipe;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        public ImageView cover;
        public TextView title;
        public TextView artist;
        public TextView numberOfSongs;
        private SwipeLayout swipeLayout;
        private TextView buttonFavorite;
        private TextView buttonCancel;


        public AlbumViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.item_album_list_cover);
            this.title = (TextView) itemView.findViewById(R.id.item_album_list_album);
            this.artist = (TextView) itemView.findViewById(R.id.item_album_list_artist);
            this.numberOfSongs = (TextView) itemView.findViewById(R.id.item_album_list_number_of_songs);
            this.swipeLayout = (SwipeLayout) itemView.findViewById(R.id.item_album_list_swipe);
            this.buttonFavorite = (TextView) itemView.findViewById(R.id.item_album_list_button_favorite);
            this.buttonCancel = (TextView) itemView.findViewById(R.id.item_album_list_button_cancel);
        }

    }
}
