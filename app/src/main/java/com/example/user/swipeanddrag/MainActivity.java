package com.example.user.swipeanddrag;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.user.swipeanddrag.Both.BothFragment;
import com.example.user.swipeanddrag.Drag.DragFragment;
import com.example.user.swipeanddrag.Swipe.SwipeFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FrameLayout layoutContainer;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseWidgets();
        setDefaultFragment(savedInstanceState);
    }
    private void initialiseWidgets(){
        toolbar = findViewById(R.id.main_toolbar);
        layoutContainer = findViewById(R.id.main_layoutcontainer);
        bottomNav = findViewById(R.id.main_bottom_nav);

        bottomNav.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.bottom_nav_swipe:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_layoutcontainer,
                        new SwipeFragment()).commit();
                toolbar.setTitle("Swipe");
                break;
            case R.id.bottom_nav_drag:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_layoutcontainer,
                        new DragFragment()).commit();
                toolbar.setTitle("Drag");
                break;
            case R.id.bottom_nav_both:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_layoutcontainer,
                        new BothFragment()).commit();
                toolbar.setTitle("Swipe & Drag");
                break;
        }
        return true;
    }

    private void setDefaultFragment(Bundle bundle){
        if(bundle == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_layoutcontainer,
                    new SwipeFragment()).commit();
            bottomNav.setSelectedItemId(R.id.bottom_nav_swipe);
            toolbar.setTitle("Swipe");
        }
    }
}
