package com.example.user.swipeanddrag.Drag;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.user.swipeanddrag.R;

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

       // View view = ((DragRVAdapter.DragRVViewHolder)viewHolder).getLayoutContainer();
       // view.setBackgroundResource(R.color.colorBackgroundCircleDefault);

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

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
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
