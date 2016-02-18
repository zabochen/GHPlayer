package ua.ck.ghplayer.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import ua.ck.ghplayer.interfaces.ItemClickListener;

public class RecyclerViewTouchListener extends GestureDetector.SimpleOnGestureListener implements RecyclerView.OnItemTouchListener {

    private Context context;
    private GestureDetector gestureDetector;
    private ItemClickListener itemClickListener;
    private RecyclerView recyclerView;

    public RecyclerViewTouchListener(Context context, ItemClickListener itemClickListener, RecyclerView recyclerView) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.recyclerView = recyclerView;
        this.gestureDetector = new GestureDetector(context, this);
    }

    // GestureDetector.SimpleOnGestureListener

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null && itemClickListener != null) {
            itemClickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
        }
    }

    // RecyclerView.OnItemTouchListener

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && itemClickListener != null && gestureDetector.onTouchEvent(e)) {
            itemClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

}
