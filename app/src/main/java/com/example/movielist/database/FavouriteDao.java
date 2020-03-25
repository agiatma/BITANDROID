package com.example.movielist.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("SELECT* FROM favourite")
    List<FavouriteEntry> loadAllTasks();

    @Query("SELECT* FROM favourite")
    LiveData<List<FavouriteEntry>> loadAllTasksLive();

    @Insert
    void insertTask(FavouriteEntry favouriteEntry);


    @Delete
    void deleteTask(FavouriteEntry favouriteEntry);

    @Query("SELECT* FROM favourite WHERE id = :id")
    FavouriteEntry loadFavById(int id);

}
