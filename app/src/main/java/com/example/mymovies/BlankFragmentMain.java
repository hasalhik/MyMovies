package com.example.mymovies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.mymovies.data.Movie;


import com.example.mymovies.databinding.FragmentBlankManiBinding;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BlankFragmentMain extends Fragment implements LoaderManager.LoaderCallbacks<JSONObject> {
    private FragmentBlankManiBinding binding;
    private MovieAdapter adapter;
    private MainViewModel viewModel;

    private static final int LOADER_ID = 123;
    private LoaderManager loaderManager;
    private static int page = 1;
    private static boolean isLoading = false;


   // public BlankFragmentMain(MainActivity mainActivity) {this.mainActivity = mainActivity;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        binding = FragmentBlankManiBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        loaderManager = LoaderManager.getInstance(this);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
                getActivity().getApplication()).create(MainViewModel.class);
        setSort();
        buildRecyclerView();

    }
    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int)(displayMetrics.widthPixels/displayMetrics.density);
        return width/185>2?width/185:2;
    }

    private void buildRecyclerView() {

        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(getSort(), page);
        if (jsonObject == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("No Connection!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
        adapter = new MovieAdapter();
        binding.recyclerViewPosters.setLayoutManager(new GridLayoutManager(this.getContext(), getColumnCount()));
        downloadData();
        LiveData<List<Movie>> moviesLiveData = viewModel.getMovies();
        moviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (page == 1)
                    adapter.setMovies(movies);

            }
        });

        binding.recyclerViewPosters.setAdapter(adapter);
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int Position) {
                ((MainActivity) getActivity()).replaceFragmentToDetail(adapter.getMovies().get(Position));

            }
        });
        adapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading)
                    downloadData();


            }
        });


    }

    private void downloadData() {
        Bundle bundle = new Bundle();
        bundle.putString("url", NetworkUtils.buildUrl(getSort(), page).toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    private void setSort() {
        descToOn();
        ascToOff();
        binding.viewAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ascToOn();
                descToOff();
                page = 1;
                buildRecyclerView();
            }
        });
        binding.viewDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descToOn();
                ascToOff();
                page = 1;
                buildRecyclerView();
            }
        });
        binding.sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                page = 1;
                buildRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private String getSort() {


        String result = null;
        switch (binding.sortSpinner.getSelectedItem().toString()) {
            case "Popularity":
                result = NetworkUtils.SORT_BY_POPULARITY;
                break;
            case "Top Rated":
                result = NetworkUtils.SORT_BY_TOP_RATED;
                break;
            case "Release Date":
                result = NetworkUtils.SORT_BY_RELEASE_DATE;
                break;
            case "Revenue":
                result = NetworkUtils.SORT_BY_REVENUE;
                break;
            case "Original Title":
                result = NetworkUtils.SORT_BY_ORIGINAL_TITLE;
                break;
        }


        if (binding.textViewDescending.getCurrentTextColor()
                == getContext().getResources().getColor(R.color.purple_500)) {
            result = result + NetworkUtils.DESC;

        } else result = result + NetworkUtils.ASC;
        return result;
    }

    private void descToOn() {
        binding.imageViewDescending.setColorFilter(getContext().getResources().getColor(R.color.purple_500));
        binding.textViewDescending.setTextColor(getContext().getResources().getColor(R.color.purple_500));
    }

    private void descToOff() {
        binding.imageViewDescending.setColorFilter(getContext().getResources().getColor(R.color.purple_700));
        binding.textViewDescending.setTextColor(getContext().getResources().getColor(R.color.purple_700));
    }

    private void ascToOn() {
        binding.imageViewAscending.setColorFilter(getContext().getResources().getColor(R.color.purple_500));
        binding.textViewAscending.setTextColor(getContext().getResources().getColor(R.color.purple_500));
    }

    private void ascToOff() {
        binding.imageViewAscending.setColorFilter(getContext().getResources().getColor(R.color.purple_700));
        binding.textViewAscending.setTextColor(getContext().getResources().getColor(R.color.purple_700));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_blank_mani, container, false);
        return binding.getRoot();
    }


    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(requireContext(), args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                binding.progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });

        return jsonLoader;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJson(data);
        binding.progressBarLoading.setVisibility(View.GONE);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                viewModel.deleteAllMovie();
                adapter.clear();
            }
            for (Movie movie : movies)
                viewModel.insertMovie(movie);
            adapter.addMovies(movies);

            page++;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("No Connection!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        isLoading = false;
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

}