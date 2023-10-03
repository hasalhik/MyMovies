package com.example.mymovies;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.data.Movie;
import com.example.mymovies.databinding.MovieItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;

    public MovieAdapter() {
        movies = new ArrayList<Movie>() {
        };
    }

    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    interface OnPosterClickListener {
        void onPosterClick(int Position);
    }

    public void setOnPosterClickListener(MovieAdapter.OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    interface OnReachEndListener {
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        //Log.d("MyLog", movie.getPosterPath());
        if (movies.size() >= 20 && position > movies.size() - 4 && onReachEndListener != null)
            onReachEndListener.onReachEnd();

        Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.default_poster).into(holder.binding.movieItemImage);
        if ((movie.getPosterPath().contains("https://image.tmdb.org/t/p/w500null"))) {
            holder.binding.movieItemText.setText(movie.getTitle());
            Picasso.get().load(R.drawable.default_poster).into(holder.binding.movieItemImage);
            holder.binding.movieItemImage.setVisibility(View.INVISIBLE);
            Log.d("MyLog", movie.getTitle());
        }

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        MovieItemBinding binding;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MovieItemBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPosterClickListener != null) {
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });

        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }
}
