package com.example.movielist.interfaces;

import com.example.movielist.model.APIModel;
import com.example.movielist.model.APIReview;
import com.example.movielist.model.APITrailer;

public interface IConnectInternet {
    void callback(APIModel obj);
    void callbackReview(APIReview obj);
    void callbackTrailer(APITrailer obj);

}
