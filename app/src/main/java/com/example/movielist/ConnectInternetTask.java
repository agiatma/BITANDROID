package com.example.movielist;

import android.os.AsyncTask;
import android.util.Log;

import com.example.movielist.interfaces.IConnectInternet;
import com.example.movielist.model.APIModel;
import com.example.movielist.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

public class ConnectInternetTask extends AsyncTask<URL, Integer, APIModel> {

    public static final String TAG = "ConnectInternetTask";

    private IConnectInternet callbackHandler = null;

    public ConnectInternetTask (IConnectInternet callback){
        this.callbackHandler = callback; }

    @Override
    protected APIModel doInBackground(URL... urls) {
        String tmpResult = "";
        try{
            tmpResult = NetworkUtils.getResponseFromHttpUrl(urls[0]);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        Log.d("Test",tmpResult);

        Gson tmpGSON = new GsonBuilder().create();
        APIModel tmpModel = tmpGSON.fromJson(tmpResult,APIModel.class);

        return tmpModel;
    }

    @Override
    protected void onPostExecute(APIModel s){
        super.onPostExecute(s);

        if(callbackHandler !=null){
            callbackHandler.callback(s);
        }

    }
}
