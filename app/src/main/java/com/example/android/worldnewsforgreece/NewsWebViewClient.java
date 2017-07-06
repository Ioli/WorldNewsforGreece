/*
 * Copyright (C) 2017 The Android Open Source Project
 * This app "World News for Greece"
 * is an app to display news for Greece from foreign papers as Guardian
 * Is created with android studio 2.3.1
 * as exercise for Android Basics by Google Nanodegree Program
 * "News Art app " by Dimitra Christina Nikolaidou
 */
package com.example.android.worldnewsforgreece;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsWebViewClient extends WebViewClient {
    private ProgressBar mProgressBar;

    public NewsWebViewClient(ProgressBar progressBar) {
        this.mProgressBar = progressBar;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mProgressBar.setVisibility(View.GONE);
    }
}
