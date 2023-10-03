package com.example.mymovies;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.databinding.MovieItemBinding;
import com.example.mymovies.databinding.ReviewItemBinding;
import com.example.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;

    public ReviewAdapter() {
        reviews = new ArrayList<Review>() {
        };
    }

    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    interface OnPosterClickListener {
        void onPosterClick(int Position);
    }

    public void setOnPosterClickListener(ReviewAdapter.OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    interface OnReachEndListener {
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        if (position > reviews.size() - 4 && onReachEndListener != null)
            onReachEndListener.onReachEnd();
        Log.d("MyLog", review.getAuthor()+"");
        holder.binding.reviewItemNameText.setText(review.getAuthor()+"\n"+"\n"+review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        ReviewItemBinding binding;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReviewItemBinding.bind(itemView);
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

    public List<Review> getReviews() {
        return reviews;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setReview(List<Review> review) {
        this.reviews = review;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addReview(List<Review> review) {
        this.reviews.addAll(review);
        notifyDataSetChanged();
    }
}
