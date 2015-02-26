package com.test.news.network.request;

import com.test.news.network.NewsRestService;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by sauravpandey on 2/23/15.
 */
public abstract class NewsSpiceRequest<R> extends RetrofitSpiceRequest<R, NewsRestService>
{
    public NewsSpiceRequest(Class<R> clazz)
    {
        super(clazz, NewsRestService.class);
    }

    abstract public String getCacheKey();

}
