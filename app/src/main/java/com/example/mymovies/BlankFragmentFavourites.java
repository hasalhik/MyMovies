package com.example.mymovies;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.Movie;
import com.example.mymovies.databinding.FragmentBlankFavoritesBinding;
import com.example.mymovies.utils.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BlankFragmentFavourites extends Fragment {
    private FragmentBlankFavoritesBinding binding;
    private MovieAdapter adapter;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBlankFavoritesBinding.inflate(getLayoutInflater());
        setAdapter();

    }


    private void setAdapter() {
        adapter = new MovieAdapter();
        binding.RVFavourites.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        binding.RVFavourites.setAdapter(adapter);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                getActivity().getApplication()).create(MainViewModel.class);
        LiveData<List<FavouriteMovie>> favouriteMovie = viewModel.getFavouritesMovies();
        favouriteMovie.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> favouriteMovies) {

                List<Movie> moviesList = new ArrayList<>();
                if (favouriteMovies != null) {
                    moviesList.addAll(favouriteMovies);
                    adapter.setMovies(moviesList);
                    Log.d("MyLog", moviesList.toString() + "");

                }
            }

        });
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int Position) {
                ((MainActivity) getActivity()).replaceFragmentToDetail(adapter.getMovies().get(Position));
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }


}