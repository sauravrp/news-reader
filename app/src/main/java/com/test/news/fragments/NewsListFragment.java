package com.test.news.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.news.R;
import com.test.news.activities.WebViewActivity;
import com.test.news.fragments.common.BaseFragment;
import com.test.news.model.News;
import com.test.news.model.ResultWrapper;
import com.test.news.network.NetworkMgr;
import com.test.news.network.listeners.OnResultListener;
import com.test.news.network.request.NewsSpiceRequest;
import com.test.news.ui.InfiniteScrollListener;
import com.test.news.utils.Utilities;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class NewsListFragment extends BaseFragment
{
    private static final String APP_STATE = "com.test.news.fragments.NewsListFragment.APP_STATE";

    private static final int OFFSET = 20;
    private static final String TAG = "NewsListFragment";

    public static class AppState implements Serializable
    {
        public int mOffset;

        public int mVisibleItem;

        public AppState()
        {
            mOffset = 0;
        }
    }


    @InjectView(R.id.listView)
    ListView mListView;

    private InfiniteScrollListener mListViewScrollListener;

    private NewsAdapter mAdapter;

    private List<News> mData = new ArrayList<News>();

    private NewsResultListener mNewsListener;

    private AppState mAppState;

    private boolean mNewFragment = true;


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if(mListView != null && mListViewScrollListener != null)
        {
            mAppState.mVisibleItem = mListViewScrollListener.getLastVisibleItem();
        }

        outState.putSerializable(APP_STATE, mAppState);
    }

    public NewsListFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setupListeners();

        if(savedInstanceState != null)
        {
            mAppState = (AppState) savedInstanceState.getSerializable(APP_STATE);
            mNewFragment = false;
        }
        else
        {
            mAppState = new AppState();
            mNewFragment = true;
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        mAdapter = new NewsAdapter(getActivity(), mData);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final News newsItem = mAdapter.getItem(position);
                WebViewActivity.startActivity(getActivity(), newsItem.title, newsItem.url);
            }
        });



        mListView.setOnScrollListener(mListViewScrollListener);

       fetchNews(0);

    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(!mNewFragment)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                   mListView.smoothScrollToPosition(mAppState.mVisibleItem);
                }
            }, 500);

        }
    }

    private void fetchNews(int offset)
    {
        mAppState.mOffset = offset;

        if(Utilities.isNetworkConnectionAvailable(getActivity().getApplicationContext()))
        {

            NewsSpiceRequest<ResultWrapper> request = NetworkMgr.getInstance().getNews(offset);
            getSpiceManager().execute(request, request.getCacheKey(), DurationInMillis.ALWAYS_EXPIRED, mNewsListener);

        }
        else
        {
            NewsSpiceRequest<ResultWrapper> request = NetworkMgr.getInstance().getNews(offset);

            try
            {
                Future<ResultWrapper> data = getSpiceManager().getDataFromCache(ResultWrapper.class, request.getCacheKey());
                data.wait(500);
                if(data.isDone())
                {
                    processResultWrapper(data.get());
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setupListeners()
    {
        mNewsListener = new NewsResultListener();

        mListViewScrollListener = new InfiniteScrollListener(OFFSET)
        {
            @Override
            public void loadMore(int page, int totalItemsCount)
            {
                Log.d("NewsListFragment", "loadMore called with offset = " + page);
                fetchNews(page*OFFSET);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;

    }

    private void processResultWrapper(ResultWrapper resultWrapper)
    {
        if( resultWrapper.results != null &&  resultWrapper.results.size() > 0)
        {
            mData.addAll(resultWrapper.results);
            mAdapter.notifyDataSetChanged();
        }
    }

    public final class NewsResultListener extends OnResultListener<ResultWrapper>
    {

        @Override
        public void onRequestNotFound()
        {
            Log.e(TAG, "requestNotFound");
        }

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            Log.e(TAG, "onRequestFailure" + spiceException.getMessage());
        }

        @Override
        public void onRequestSuccess(ResultWrapper resultWrapper)
        {
            Log.d(TAG, "success, results = " + resultWrapper.num_results);

           processResultWrapper(resultWrapper);
        }
    }

    static class ItemViewHolder
    {
        @InjectView(R.id.imagePreview)
        ImageView imageView;

        @InjectView(R.id.newsTitle)
        TextView newsTitle;

        @InjectView(R.id.newsAuthor)
        TextView newsAuthor;

       public ItemViewHolder(View view)
       {
           ButterKnife.inject(this, view);
       }
    }

    public static class NewsAdapter extends ArrayAdapter<News>
    {

        public NewsAdapter(Context context, List<News> items)
        {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemViewHolder viewHolder;

            if(convertView == null)
            {
                convertView = inflater.inflate(R.layout.item_news_item, null);
                viewHolder = new ItemViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ItemViewHolder) convertView.getTag();
            }

            final News newsItem = getItem(position);

            if(!TextUtils.isEmpty(newsItem.title))
              viewHolder.newsTitle.setText(newsItem.title);
            if(!TextUtils.isEmpty(newsItem.byline))
                viewHolder.newsAuthor.setText(newsItem.byline);

            if(newsItem.media != null && newsItem.media.size() > 0)
            {
                final String url = newsItem.media.get(0).url;
                Log.d("NewsListFragment" , "Loading Url = " + url);
                Picasso.with(getContext()).load(url).into(viewHolder.imageView);
            }

            return convertView;

        }
    }


}