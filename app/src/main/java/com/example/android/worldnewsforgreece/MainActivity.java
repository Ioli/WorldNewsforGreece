/*
 * Copyright (C) 2017 The Android Open Source Project
 * This app "World News for Greece"
 * is an app to display news for Greece from foreign papers as Guardian
 * Is created with android studio 2.3.1
 * as exercise for Android Basics by Google Nanodegree Program
 * "News Art app " by Dimitra Christina Nikolaidou
 */

package com.example.android.worldnewsforgreece;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {


    /**
     * URL for news data from the guardian API dataset
     */
    private static final String link =
            "https://content.guardianapis.com/search";

    private static final String apiKey = "test";

    /**
     * Adapter for the list of news
     */
    private NewsRecyclerAdapter mAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find a reference to the {@link RecyclerView} in the layout
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            mEmptyStateTextView.setVisibility(View.GONE);
            mAdapter = new NewsRecyclerAdapter(MainActivity.this, new ArrayList<News>());
            mRecyclerView.setAdapter(mAdapter);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(1, null, MainActivity.this);
        } else {
            ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internet connection.");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    public void startWebView(String url) {
        Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
        webIntent.setData(Uri.parse(url));
        startActivity(webIntent);
    }


    public static class NewsLoader extends AsyncTaskLoader<List<News>> {

        /**
         * Query URL
         */
        private String mUrl;

        /**
         * Constructs a new {@link NewsLoader}.
         *
         * @param context of the activity
         * @param url     to load data from
         */
        public NewsLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<News> loadInBackground() {
            if (mUrl == null) {
                return null;
            }
            // Perform the network request, parse the response, and extract a list of articles.
            List<News> news = QueryUtils.fetchNewsData(mUrl);
            return news;
        }
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPrefs.getString(
                getString(R.string.section),
                getString(R.string.section));

        String viewSize = sharedPrefs.getString(
                getString(R.string.viewSize),
                getString(R.string.viewSizeDefault)
        );
        Uri baseUri = Uri.parse(link);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", section);
        uriBuilder.appendQueryParameter("page-size", viewSize);
        uriBuilder.appendQueryParameter("api-key", apiKey);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter = new NewsRecyclerAdapter(MainActivity.this, new ArrayList<News>());

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the RecycleView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter = new NewsRecyclerAdapter(MainActivity.this, news);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can renew out our existing data.
        mAdapter = new NewsRecyclerAdapter(MainActivity.this, new ArrayList<News>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
