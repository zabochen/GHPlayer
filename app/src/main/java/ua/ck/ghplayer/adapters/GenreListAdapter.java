package ua.ck.ghplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.models.Genre;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.GenreViewHolder>{
    private ArrayList data;

    public GenreListAdapter(ArrayList data) {
        super();
        this.data = data;
    }

    @Override
    public GenreListAdapter.GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_genre_list, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreListAdapter.GenreViewHolder holder, int position) {
        Genre genre = (Genre)data.get(position);
        //holder.cover.setImageBitmap();
        holder.name.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {
        public ImageView cover;
        public TextView name;


        public GenreViewHolder(View itemView) {
            super(itemView);
            this.cover = (ImageView) itemView.findViewById(R.id.item_genre_list_cover);
            this.name = (TextView) itemView.findViewById(R.id.item_genre_list_name);
        }
    }

}
