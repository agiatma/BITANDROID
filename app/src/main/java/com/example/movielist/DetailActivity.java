package com.example.movielist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.movielist.database.FavouriteDatabase;
import com.example.movielist.database.FavouriteEntry;
import com.example.movielist.interfaces.IConnectInternet;
import com.example.movielist.model.APIModel;
import com.example.movielist.model.APIReview;
import com.example.movielist.model.APITrailer;
import com.example.movielist.model.Review;
import com.example.movielist.model.Trailer;
import com.example.movielist.utilities.ConnectInternetReview;
import com.example.movielist.utilities.ConnectInternetTrailer;
import com.example.movielist.utilities.NetworkUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import static com.example.movielist.utilities.NetworkUtils.buildReview;
import static com.example.movielist.utilities.NetworkUtils.buildTrailer;


public class DetailActivity extends AppCompatActivity implements IConnectInternet {



    Menu favMenu;
    boolean isfavourite = false;

    private FavouriteDatabase mDb;

    TextView tvTitle;
    TextView tvDate;
    TextView tvRating;
    TextView tvOverview;
    TextView tvReview;
    TextView tvTrailer;
    String imgurl;
    ImageView img;


    Context context = this;

    int id = 0;
    public URL urlReview;
    public URL urlTrailer;

    ArrayList<Review> reviewData = new ArrayList<>();
    ArrayList<Trailer> trailerData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bind layout id to detail activity
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tv_title);
        tvDate = findViewById(R.id.tv_date);
        tvRating = findViewById(R.id.tv_rating);
        tvOverview = findViewById(R.id.tv_overview);
        img = findViewById(R.id.img_item_photo);
        tvTrailer = findViewById(R.id.tv_trailer);
        tvReview = findViewById(R.id.tv_review);

        //Get data from bundle intent
        Bundle bundle = getIntent().getExtras();
        String idx = bundle.getString("id");
        try {
            id = Integer.parseInt(idx);
        } catch(NumberFormatException nfe) {

        }
        Log.d("ADIT",id+"");
        tvTitle.setText(bundle.getString("title"));
        tvDate.setText(bundle.getString("date"));
        tvRating.setText(bundle.getString("rating"));
        imgurl= bundle.getString("img");
        Glide.with(DetailActivity.this).load("https://image.tmdb.org/t/p/w185/"+
                imgurl).into(img);
        tvOverview.setText(bundle.getString("overview"));

        //bind DB to detail activity
        mDb = FavouriteDatabase.getInstance(getApplicationContext());
        //Load FavDB cek untuk cek apakah film tersebut ada di DB
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavouriteEntry favouriteEntry = mDb.favouriteDao().loadFavById(id);
                if (favouriteEntry != null){
                    isfavourite = true;

                }
            }
        });

        //Fetch data from API Trailer
        try {
            urlTrailer = buildTrailer(idx);

        }catch (MalformedURLException e) {
            String textToShow = "URL gagal build";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
        }

        if(urlTrailer != null){
            new ConnectInternetTrailer(this).execute(urlTrailer);

        }try {
            buildTrailer(idx);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Fetch data from API Review
        try {
            urlReview = buildReview(idx);

        }catch (MalformedURLException e) {
            String textToShow = "URL gagal build";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
        }

        if(urlReview != null){
            new ConnectInternetReview(this).execute(urlReview);

        }try {
            buildReview(idx);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu favbtn){
        favMenu = favbtn;
        getMenuInflater().inflate(R.menu.tool_bar,favbtn);
        setIconFav(isfavourite);
        return super.onCreateOptionsMenu(favbtn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Ambil Data dari detail activity untuk disimpan di favDB
        mDb = FavouriteDatabase.getInstance(getApplicationContext());
        String title = tvTitle.getText().toString();
        String date = tvDate.getText().toString();
        String rating = tvRating.getText().toString();
        String overview = tvOverview.getText().toString();
        final FavouriteEntry favEntry = new FavouriteEntry(id,title,date,rating,overview,imgurl);

        Log.d("Paporit", isfavourite+"");
        if(!isfavourite){
            Toast.makeText(DetailActivity.this, "Movie add to Favourite", Toast.LENGTH_SHORT).show();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.favouriteDao().insertTask(favEntry);
                    Log.d("SIMPAN", "Data tersimpan");
                }
            });
        } else {
            Toast.makeText(DetailActivity.this, "Movie Removed from Favourite", Toast.LENGTH_SHORT).show();
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.favouriteDao().deleteTask(favEntry);
                    Log.d("HAPUS", "Data terhapus");
                }
            });

        } setIconFav(!isfavourite);
        return true;
    }

    private  void setIconFav(Boolean isfavourite){
        MenuItem item = favMenu.getItem(0);

        if (isfavourite){
            Log.d("Paporit","garis");
            item.setIcon(R.drawable.ic_favorite_black);
            this.isfavourite=true;
        }else {
            Log.d("Paporit","hitam");
            item.setIcon(R.drawable.ic_favorite);
            this.isfavourite=false;
        }

    }

    @Override
    public void callback(APIModel obj) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void callbackReview(APIReview obj) {
        reviewData = obj.getResults();
        Log.d("GILA",reviewData+"");
        String plus ="";
        Set<String> ripiu= new LinkedHashSet<>();

        for(Review review : reviewData) {
            String repiu = "";
            String penulis = review.getAuthor();
            String konten = review.getContent();
            Set<String> join = new LinkedHashSet<>();
            join.add(penulis);
            join.add(konten);
            repiu = repiu.join("\n", join);

            ripiu.add(repiu);
        }
        plus = plus.join("\n\n",ripiu);
        tvReview.setText(plus);
        }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void callbackTrailer(APITrailer obj) {
        trailerData = obj.getResults();

        Log.d("GILA",trailerData+"");
        Set<String> gabung = new LinkedHashSet<>();
        String trailerAll = "";

        for (Trailer link : trailerData) {
            String key = link.getKey();
            String youtubeURL = "https://www.youtube.com/watch?v="+key;

            gabung.add(youtubeURL);

        }
        trailerAll = trailerAll.join("\n\n",gabung);
        tvTrailer.setText(trailerAll);
    }

}
