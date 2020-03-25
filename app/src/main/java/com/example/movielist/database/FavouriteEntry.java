package com.example.movielist.database;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName ="favourite")
public class FavouriteEntry {

    @PrimaryKey
    private int id;
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String overview;
    private String posterPath;


    public FavouriteEntry(int id,String title, String releaseDate, String voteAverage, String overview, String posterPath){
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
//    public FavouriteEntry(int id, String title, String releaseDate, String voteAverage, String overview){
//        this.id = id;
//        this.title = title;
//        this.releaseDate = releaseDate;
//        this.overview = overview;
//        //this.posterPath = posterPath;
//    }


   // public String getPosterPath(){return posterPath;}
}
