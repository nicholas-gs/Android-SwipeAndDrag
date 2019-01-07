package com.example.user.swipeanddrag.Drag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;


public class DragRVTouchHelper extends ItemTouchHelper.SimpleCallback {

    private DragRVTouchHelperListener listener;

    public interface DragRVTouchHelperListener {
        void onMove(int originalPos, int newPos);
    }

    //Constructor
    public DragRVTouchHelper(DragRVTouchHelperListener listener, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        // Do nothing since we disable swiping
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        listener.onMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    // Enable dragging
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    // Disable swiping
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}
