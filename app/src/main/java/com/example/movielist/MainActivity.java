package com.example.movielist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.movielist.database.FavouriteDatabase;
import com.example.movielist.database.FavouriteEntry;
import com.example.movielist.interfaces.IConnectInternet;
import com.example.movielist.model.APIModel;
import com.example.movielist.model.APIReview;
import com.example.movielist.model.APITrailer;
import com.example.movielist.model.TopRated;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.movielist.utilities.NetworkUtils.buildUrl;

public class MainActivity extends AppCompatActivity implements IConnectInternet, GridMovieAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    URL url = null;
    RecyclerView recyclerView;
    GridMovieAdapter mAdapter;
    ArrayList<TopRated> result = new ArrayList<>();

    private final static String MENU_SELECTED = "selected";
    private int selected;

    final String POPMOVIEDB_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=bdd497cb1f3fadbdace5b3d6becea086";
    final String TOPMOVIEDB_URL =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=bdd497cb1f3fadbdace5b3d6becea086";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            selected = savedInstanceState.getInt(MENU_SELECTED);
            setMode(selected);
        } else {
            movieUrl(POPMOVIEDB_URL);
        }
        //inisiasi recycler view sesuai layout xml
        recyclerView = findViewById(R.id.rv_movie);
        // call adapter
        mAdapter = new GridMovieAdapter(this, this);
        // call layout manager
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //set adapter
        recyclerView.setAdapter(mAdapter);
        //Load API MOVIEDB POPULAR
    }

    //menu dari sort
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //deteksi menubar pilih yang mana
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    //set mode ketika menu dipilih
    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.top_rated_sort:
                movieUrl(TOPMOVIEDB_URL);
                selected = selectedMode;
                break;

            case R.id.popular_sort:
                movieUrl(POPMOVIEDB_URL);
                selected = selectedMode;
                break;

            case R.id.favorite_sort:
                setupViewMode();
                break;
        }
    }

    public void movieUrl(String movieUrl) {
        try {
            url = buildUrl(movieUrl);
        } catch (MalformedURLException e) {
        }
        if (url != null) {
            new ConnectInternetTask(this).execute(url);
        }
        try {
            buildUrl(movieUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //set data dari API
    @Override
    public void callback(APIModel obj) {
        result = obj.getResults();
        //set data array dari fungsi adapter setMovieList
        Log.d("ADIT",result.toString());
        mAdapter.setMovieList(result);
    }

    @Override
    public void callbackReview(APIReview obj) {

    }

    @Override
    public void callbackTrailer(APITrailer obj) {

    }

    @Override
    public void onListItemClicked(TopRated o) {
        Toast.makeText(this, o.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public void setupViewMode(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFav().observe(this, new Observer<List<FavouriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteEntry> favouriteEntries) {
                Log.d(TAG,"updatinglist of tasks from LiveData in ViewModel");
                final ArrayList<TopRated> favourite = new ArrayList<>();
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Load All Database about favorite film
                        List<FavouriteEntry> favouriteEntries = FavouriteDatabase.
                                getInstance(getApplicationContext()).favouriteDao().loadAllTasks();
                        //using for each to add db to arraylist toprated
                        for (FavouriteEntry favouriteEntry : favouriteEntries) {
                            TopRated a = new TopRated();
                            a.setId(favouriteEntry.getId());
                            a.setTitle(favouriteEntry.getTitle());
                            a.setReleaseDate(favouriteEntry.getReleaseDate());
                            a.setVoteAverage(Double.parseDouble(favouriteEntry.getVoteAverage()));
                            a.setOverview(favouriteEntry.getOverview());
                            a.setPosterPath(favouriteEntry.getPosterPath());
                            favourite.add(a);
                        }
                    }
                });
                mAdapter.setMovieList(favourite);
            }
        });

        }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(MENU_SELECTED, selected);
    }

}
