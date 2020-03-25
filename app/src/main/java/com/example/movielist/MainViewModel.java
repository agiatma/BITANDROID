package com.example.movielist;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movielist.database.FavouriteDatabase;
import com.example.movielist.database.FavouriteEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavouriteEntry>> fav;

    public MainViewModel (Application application){
        super(application);
        FavouriteDatabase database = FavouriteDatabase.getInstance(this.getApplication());
        fav = database.favouriteDao().loadAllTasksLive();
    }

    public LiveData<List<FavouriteEntry>> getFav(){
        return fav;
    }
}
