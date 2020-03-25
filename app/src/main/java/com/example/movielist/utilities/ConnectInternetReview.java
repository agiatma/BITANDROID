package com.example.movielist.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.movielist.interfaces.IConnectInternet;
import com.example.movielist.model.APIReview;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

public class ConnectInternetReview extends AsyncTask<URL, Integer, APIReview> {

    public static final String TAG = "ConnectInternetTask";

    private IConnectInternet callbackHandler = null;

    public ConnectInternetReview(IConnectInternet callback){
        this.callbackHandler = callback;
    }

    @Override
    protected APIReview doInBackground(URL... urls) {
        String tmpResult = "";
        try{
            tmpResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        Log.d("Test",tmpResult);

        Gson tmpGSON = new GsonBuilder().create();
        APIReview tmpModel = tmpGSON.fromJson(tmpResult,APIReview.class);

        return tmpModel;
    }

    @Override
    protected void onPostExecute(APIReview s){
        super.onPostExecute(s);

        if(callbackHandler !=null){
            callbackHandler.callbackReview(s);
        }

    }



}
