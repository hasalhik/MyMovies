package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;


import com.example.mymovies.data.Movie;
import com.example.mymovies.databinding.ActivityMainBinding;
import com.example.mymovies.databinding.FragmentBlankDetailBinding;
import com.example.mymovies.utils.NetworkUtils;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private ActivityMainBinding binding;


    private BlankFragmentDetail blankFragmentDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideActionBar();
        setButtonMenu();
        if (savedInstanceState != null) {
            blankFragmentDetail = (BlankFragmentDetail) getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");

        } else {

            getSupportFragmentManager().beginTransaction().replace(R.id.placeHolder, new BlankFragmentMain()).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (blankFragmentDetail != null)
            if (blankFragmentDetail.isAdded()) {
                //Save the fragment's instance
                Log.e("MyLod", blankFragmentDetail.toString());
                getSupportFragmentManager().putFragment(outState, "myFragmentName", blankFragmentDetail);
            }
    }


    private void setButtonMenu() {

        binding.btNavigationMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new BlankFragmentMain()).commit();
                        break;
                    case R.id.favorites:
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, new BlankFragmentFavourites()).commit();
                        break;
                }

                return false;
            }
        });

    }


    private void hideActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }


    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

    public void replaceFragmentToDetail(Movie movie) {
        blankFragmentDetail = new BlankFragmentDetail(movie);
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.placeHolder, blankFragmentDetail).commit();
    }
}