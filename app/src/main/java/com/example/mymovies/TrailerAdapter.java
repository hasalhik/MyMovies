package com.example.mymovies;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.data.Trailer;
import com.example.mymovies.databinding.TrailerItemBinding;
import com.example.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> trailers;

    public TrailerAdapter() {
        trailers = new ArrayList<Trailer>() {
        };
    }

    private OnTrailerClickListener onTrailerClickListener;
    private OnReachEndListener onReachEndListener;

    interface OnTrailerClickListener {
        void onTrailerClick(String url);
    }

    public void setOnTrailerClickListener(TrailerAdapter.OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    interface OnReachEndListener {
        void onReachEnd();
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);

        if (position > trailers.size() - 4 && onReachEndListener != null)
            onReachEndListener.onReachEnd();
        //Log.d("MyLog", NetworkUtils.buildPathToPreview(trailer.getKeyOfTrailer())+"");

        Picasso.get().load(NetworkUtils.buildPathToPreview(trailer.getKeyOfTrailer())).placeholder(R.drawable.default_you_tube_preview).into(holder.binding.trailerItemImage);
        holder.binding.trailerItemText.setText(trailer.getNameVideo());
        //holder.binding.trailerItemText.setHeight(holder.binding.trailerItemText.getWidth()*16/9);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder {
        TrailerItemBinding binding;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TrailerItemBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTrailerClickListener != null) {
                        onTrailerClickListener.onTrailerClick(trailers.get(getAdapterPosition()).getLinkToVideo());
                    }
                }
            });

        }
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTrailer(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addTrailer(List<Trailer> trailers) {
        this.trailers.addAll(trailers);
        notifyDataSetChanged();
    }
}
