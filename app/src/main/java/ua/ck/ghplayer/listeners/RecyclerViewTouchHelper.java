package ua.ck.ghplayer.listeners;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import ua.ck.ghplayer.adapters.TrackListAdapter;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TrackListAdapter adapter;

    public RecyclerViewTouchHelper(TrackListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.addToPlaylist(viewHolder.getAdapterPosition());
        adapter.notifyDataSetChanged();
    }
}
