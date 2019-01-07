package com.example.user.swipeanddrag.Drag;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import java.util.Collections;

public class DragFragment extends Fragment implements DragRVTouchHelper.DragRVTouchHelperListener {

    private RecyclerView recyclerView;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RequestQueue requestQueue;
    private String url;
    private Context context;
    private DragRVAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drag_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        recyclerView = view.findViewById(R.id.drag_recyclerview);

        setupRecyclerView();
        parseJson(url);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.SimpleCallback simpleCallback = new DragRVTouchHelper(this, ItemTouchHelper.UP |
                ItemTouchHelper.DOWN, 0);
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
                        adapter = new DragRVAdapter(context, contactArrayList);
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

    @Override
    public void onMove(int originalPos, int newPos) {

        if (originalPos < newPos) {
            //Drag item downwards
            for (int i = originalPos; i < newPos; i++) {
                Collections.swap(contactArrayList, i, i + 1);
                adapter.notifyItemMoved(i, i + 1);
            }
        } else if (originalPos > newPos) {
            //User drag up
            for (int i = originalPos; i > newPos; i--) {
                Collections.swap(contactArrayList, i, i - 1);
                adapter.notifyItemMoved(i, i - 1);
            }
        }
    }
}
