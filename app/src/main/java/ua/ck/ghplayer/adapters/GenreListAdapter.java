package ua.ck.ghplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.ck.ghplayer.R;
import ua.ck.ghplayer.lists.GenreList;

public class GenreListAdapter extends RecyclerView.Adapter<GenreListAdapter.MyViewHolder> {


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_genre_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(GenreList.getInstance().getGenreList().get(position).getName());
    }

    @Override
    public int getItemCount() {
        return GenreList.getInstance().getGenreList() != null ? GenreList.getInstance().getGenreList().size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // View
        public TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.item_genre_list_name);
        }
    }

}
