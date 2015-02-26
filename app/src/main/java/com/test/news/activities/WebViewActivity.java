package com.test.news.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class WebViewActivity extends Activity
{
    public static final String URL = "URL";
    public static final String TITLE = "TITLE";

    public static void startActivity(Context context, String title, String url)
    {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    WebView mWebView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
       // mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.getSettings().setUseWideViewPort(true);
        //mWebView.getSettings().setBuiltInZoomControls(true);


        if(getIntent().hasExtra(TITLE))
        {
           // setTitle(getIntent().getStringExtra(TITLE));
        }

        String url = "";

        if(getIntent().hasExtra(URL))
        {
            url = getIntent().getStringExtra(URL);
        }
        if(!TextUtils.isEmpty(url))
        {
            mWebView.loadUrl(url);
        }

        setContentView(mWebView);
    }


}