package com.example.user.swipeanddrag.Drag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.swipeanddrag.Contact;
import com.example.user.swipeanddrag.R;

import java.util.ArrayList;

public class DragRVAdapter extends RecyclerView.Adapter<DragRVAdapter.DragRVViewHolder> {

    private Context context;
    private ArrayList<Contact> contactArrayList;

    // Constructor
    public DragRVAdapter(Context context, ArrayList<Contact> contactArrayList) {
        this.context = context;
        this.contactArrayList = contactArrayList;
    }

    public class DragRVViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView,emailTextView;
        private ImageView dragImageView;
        private RelativeLayout layoutContainer;

        public DragRVViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.drag_item_name);
            emailTextView = itemView.findViewById(R.id.drag_item_email);
            dragImageView = itemView.findViewById(R.id.drag_item_handle);
            layoutContainer = itemView.findViewById(R.id.drag_foreground_container);
        }

        public RelativeLayout getLayoutContainer(){
            return layoutContainer;
        }
    }

    @NonNull
    @Override
    public DragRVViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.drag_item_container,viewGroup,false);
        return new DragRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DragRVViewHolder dragRVViewHolder, int i) {
        Contact currentContact = contactArrayList.get(i);
        dragRVViewHolder.nameTextView.setText(currentContact.getName());
        dragRVViewHolder.emailTextView.setText(currentContact.getEmailAddress());
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

}
