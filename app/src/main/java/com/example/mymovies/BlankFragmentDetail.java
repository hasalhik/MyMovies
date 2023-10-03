package com.example.mymovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymovies.data.FavouriteMovie;
import com.example.mymovies.data.Movie;
import com.example.mymovies.databinding.FragmentBlankDetailBinding;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;


public class BlankFragmentDetail extends Fragment {
    private FragmentBlankDetailBinding binding;
    private Movie movie;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    private MainViewModel viewModel;

    public BlankFragmentDetail(Movie movie) {
        this.movie = movie;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBlankDetailBinding.inflate(getLayoutInflater());
        setViewModel();

        if (savedInstanceState != null)
            movie = viewModel.getMovieById(savedInstanceState.getInt("movieId"));
        if (movie != null)
            setAllInfo();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putInt("movieId", movie.getId());
    }

    private void setViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                getActivity().getApplication()).create(MainViewModel.class);
    }


    public BlankFragmentDetail() {

    }

    private void setAllInfo() {
        binding.scrollView.smoothScrollTo(0, 0);
        binding = FragmentBlankDetailBinding.inflate(getLayoutInflater());
        Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.default_poster).into(binding.imageView);
        binding.movieInfo.textViewTitle.setText(movie.getTitle());
        binding.movieInfo.textViewOriginalTitle.setText(movie.getOriginalTitle());
        binding.movieInfo.textViewOverview.setText(movie.getOverview());
        binding.movieInfo.textViewReleaseDate.setText(movie.getReleaseDate());
        binding.movieInfo.textViewRating.setText(Double.toString(movie.getVoteAverage()));

        FavouriteMovie favouriteMovie = viewModel.getMovieFavouriteById(movie.getId());
        if (favouriteMovie != null) favorToOn();
        else favorToOff();
        binding.imageViewAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouriteMovie favouriteMovie1 = viewModel.getMovieFavouriteById(movie.getId());
                if (favouriteMovie1 != null) {
                    favorToOff();
                    viewModel.deleteFavouriteMovie(favouriteMovie1);
                } else {
                    favorToOn();
                    viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
                }

            }
        });
        trailerAdapter = new TrailerAdapter();
        reviewAdapter = new ReviewAdapter();

        reviewAdapter.setReview(JSONUtils.getReviewsFromJson(NetworkUtils.getJSONForReviews(movie.getId())));
        Log.d("MyLog", NetworkUtils.getJSONForReviews(movie.getId()) + "");
        trailerAdapter.setTrailer(JSONUtils.getTrailerFromJson(NetworkUtils.getJSONForTrailer(movie.getId())));

        binding.movieInfo.RVReviews.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.movieInfo.RVReviews.setAdapter(reviewAdapter);

        binding.movieInfo.RVTrailers.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        ;
        binding.movieInfo.RVTrailers.setAdapter(trailerAdapter);
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }

    private void favorToOn() {
        binding.imageViewAddToFavourites.setColorFilter(getContext().getResources().getColor(R.color.read));

    }

    private void favorToOff() {
        binding.imageViewAddToFavourites.setColorFilter(getContext().getResources().getColor(R.color.purple_700));
    }
}