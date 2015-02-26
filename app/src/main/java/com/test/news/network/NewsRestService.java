package com.test.news.network;

import com.test.news.model.ResultWrapper;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by sauravpandey on 2/23/15.
 */
public interface NewsRestService
{
    @GET("/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873")
    ResultWrapper getNews(@Query("offset") int offset);
}