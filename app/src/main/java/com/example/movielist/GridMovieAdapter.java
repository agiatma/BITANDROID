package com.example.movielist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movielist.model.Review;
import com.example.movielist.model.TopRated;
import java.util.ArrayList;

public class GridMovieAdapter extends RecyclerView.Adapter<GridMovieAdapter.MovieViewHolder> {

    Context context;
    private ArrayList<TopRated> dataList = new ArrayList<>();

    private final ListItemClickListener<TopRated> mOnclickListener;

    public interface ListItemClickListener<T>{
        void onListItemClicked(TopRated t);
    }

    public GridMovieAdapter(Context context, ListItemClickListener onClickListener){
        this.context = context;
        this.mOnclickListener = onClickListener;
    }
    //Ambil data dari API dan diset ke variable datalist
    public void setMovieList(ArrayList<TopRated> movieList){
        dataList = new ArrayList<>();
        dataList = movieList;
        notifyDataSetChanged();
    }




    public class MovieViewHolder extends RecyclerView.ViewHolder{
        //constructor viewHolder
        private ImageView image;
        private View itemView;
        TextView tvJudul;
//        TextView tvTitle, tvDate, tvRating, tvOverview;

        public MovieViewHolder(View itemView){
            super(itemView);
            this.itemView=itemView;
            image = itemView.findViewById(R.id.img_item_photo);
            tvJudul = itemView.findViewById(R.id.tv_judul);

//            tvTitle = itemView.findViewById(R.id.tv_title);
//            tvDate = itemView.findViewById(R.id.tv_date);
//            tvRating = itemView.findViewById(R.id.tv_rating);
//            tvOverview = itemView.findViewById(R.id.tv_overview);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_grid_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        final String id = dataList.get(position).getId().toString();
        Glide.with(context).load("https://image.tmdb.org/t/p/w185/" + dataList.get(position).getPosterPath()).into(holder.image);
        holder.tvJudul.setText(dataList.get(position).getTitle());
        final String title = dataList.get(position).getTitle();
        final String date = dataList.get(position).getReleaseDate();
        final String rating = dataList.get(position).getVoteAverage().toString();
        final String overview = dataList.get(position).getOverview();
        final String img = dataList.get(position).getPosterPath();


        //LAST CODE 17/03/20 9.32 want to create intent and bundle
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mOnclickListener.onListItemClicked(dataList.get(position));
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("title", title);
                bundle.putString("date", date);
                bundle.putString("rating", rating);
                bundle.putString("overview", overview);
                bundle.putString("img", img);

                Intent newIntent = new Intent(context, DetailActivity.class);
                newIntent.putExtras(bundle);
                context.startActivity(newIntent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return (dataList != null)? dataList.size() : 0;
    }


}