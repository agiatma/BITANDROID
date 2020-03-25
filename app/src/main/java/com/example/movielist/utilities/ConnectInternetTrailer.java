package com.example.movielist.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.example.movielist.interfaces.IConnectInternet;
import com.example.movielist.model.APITrailer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

public class ConnectInternetTrailer extends AsyncTask<URL, Integer, APITrailer> {
    public static final String TAG = "ConnectInternetTask";
    private IConnectInternet callbackHandler = null;

    public ConnectInternetTrailer (IConnectInternet callback){
        this.callbackHandler = callback;
    }

    @Override
    protected APITrailer doInBackground(URL... urls) {
        String tmpResult = "";
        try{
            tmpResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        Log.d("Test",tmpResult);

        Gson tmpGSON = new GsonBuilder().create();
        APITrailer tmpModel = tmpGSON.fromJson(tmpResult,APITrailer.class);

        return tmpModel;
    }

    @Override
    protected void onPostExecute(APITrailer s){
        super.onPostExecute(s);

        if(callbackHandler !=null){
            callbackHandler.callbackTrailer(s);
        }

    }
}
