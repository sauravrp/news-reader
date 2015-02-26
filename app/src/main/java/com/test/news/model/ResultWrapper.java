package com.test.news.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class ResultWrapper
{
    @Expose
    public String status;
    @Expose
    public String copyright;

    @Expose
    public int num_results;

    @Expose
    public List<News> results;
}
