package com.example.user.swipeanddrag.Swipe;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.swipeanddrag.Contact;
import com.example.user.swipeanddrag.R;

import java.util.ArrayList;

public class SwipeRVAdapter extends RecyclerView.Adapter<SwipeRVAdapter.SwipeRVViewHolder> {

    private ArrayList<Contact> contacts;
    private Context context;

    public SwipeRVAdapter(ArrayList<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    public class SwipeRVViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout foregroundContainer,backgroundContainer;
        private TextView nameTextView, emailAddressTextView;

        public SwipeRVViewHolder(@NonNull View itemView) {
            super(itemView);
            foregroundContainer = itemView.findViewById(R.id.swipe_foreground_container);
            backgroundContainer = itemView.findViewById(R.id.swipe_background_container);
            nameTextView = itemView.findViewById(R.id.swipe_item_name);
            emailAddressTextView = itemView.findViewById(R.id.swipe_item_email);
        }

        public RelativeLayout getForegroundContainer() {
            return foregroundContainer;
        }

       /* public RelativeLayout getBackgroundContainer() {
            return backgroundContainer;
        }*/
    }

    @NonNull
    @Override
    public SwipeRVViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.swipe_item_container, viewGroup, false);
        return new SwipeRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeRVViewHolder swipeRVViewHolder, int i) {
        Contact currentContact = contacts.get(i);
        swipeRVViewHolder.nameTextView.setText(currentContact.getName());
        swipeRVViewHolder.emailAddressTextView.setText(currentContact.getEmailAddress());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    /**
     * Remove an item from the recyclerview at index *position*
     * @param position
     */
    public void removeSwipeItem(int position){
        contacts.remove(position);
        this.notifyItemRemoved(position);
    }

    /**
     * Add a Contact *contact* into the recyclerview at index *position*
     * @param position
     * @param contact
     */
    public void addSwipeItem(int position, Contact contact){
        contacts.add(position,contact);
        this.notifyItemInserted(position);
    }


}
