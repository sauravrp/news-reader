package com.test.news.network;

import com.test.news.model.ResultWrapper;
import com.test.news.network.request.NewsSpiceRequest;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class NetworkMgr
{
    private static NetworkMgr sInstance = null;

    private NetworkMgr()
    {

    }

    public static NetworkMgr getInstance()
    {
        if(sInstance == null)
        {
            sInstance = new NetworkMgr();
        }

        return sInstance;
    }



    public NewsSpiceRequest<ResultWrapper> getNews(final int offset)
    {
       return new NewsSpiceRequest<ResultWrapper>(ResultWrapper.class)
        {
            @Override
            public String getCacheKey()
            {
                return "getNews?offset=" + offset;
            }

            @Override
            public ResultWrapper loadDataFromNetwork() throws Exception
            {
               return getService().getNews(offset);
            }
        };
    }


}
