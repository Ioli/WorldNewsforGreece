/*
 * Copyright (C) 2017 The Android Open Source Project
 * This app "World News for Greece"
 * is an app to display news for Greece from foreign papers as Guardian
 * Is created with android studio 2.3.1
 * as exercise for Android Basics by Google Nanodegree Program
 * "News Art app " by Dimitra Christina Nikolaidou
 */
package com.example.android.worldnewsforgreece;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        String url = intent.getData().toString();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.web_progress_bar);
        webView.setWebViewClient(new NewsWebViewClient(progressBar));
        webView.loadUrl(url);
    }
}
