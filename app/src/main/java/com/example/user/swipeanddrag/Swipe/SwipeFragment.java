package com.example.user.swipeanddrag.Swipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.user.swipeanddrag.Contact;
import com.example.user.swipeanddrag.R;
import com.example.user.swipeanddrag.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwipeFragment extends Fragment implements SwipeRVTouchHelper.SwipeRVTouchHelperListener {

    private RecyclerView recyclerView;
    private SwipeRVAdapter adapter;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RequestQueue requestQueue;
    private String url;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.swipe_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.swipe_recyclerview);

        setupRecyclerView();
        parseJson(url);
    }

    private void setupRecyclerView(){
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        // Enable both left and right swipe
        // Pass 0 to prevent drag
        ItemTouchHelper.SimpleCallback simpleCallback = new SwipeRVTouchHelper(this,0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        url = getString(R.string.url);
        requestQueue = VolleySingleton.checkInstance(context.getApplicationContext()).getRequestQueue();

    }

    private void parseJson(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");

                                contactArrayList.add(new Contact(name, email));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Since Volley is async, pass arraylist to adapter here.
                        adapter = new SwipeRVAdapter(contactArrayList,context);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Determine what happens after the item in recyclerview is swiped off
     * @param viewHolder
     * @param direction
     * @param position
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        // Temporary store the swiped off item
        final Contact contact = contactArrayList.get(position);
        //Remove the item
        adapter.removeItem(position);
        // If swipe left - delete the item
        if(direction == ItemTouchHelper.LEFT){
            Snackbar.make(recyclerView, "Contact deleted", Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(context, R.color.colorIconTintSelected))
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.addItem(position, contact);
                        }
                    }).show();
        } // If swipe left - archive the item
        else if (direction == ItemTouchHelper.RIGHT){
            Snackbar.make(recyclerView, "Contact archive", Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(context, R.color.colorIconTintSelected))
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.addItem(position, contact);
                        }
                    }).show();
        }
    }
}
