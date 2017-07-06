/*
 * Copyright (C) 2017 The Android Open Source Project
 * This app "World News for Greece"
 * is an app to display news for Greece from foreign papers as Guardian
 * Is created with android studio 2.3.1
 * as exercise for Android Basics by Google Nanodegree Program
 * "News Art app " by Dimitra Christina Nikolaidou
 */
package com.example.android.worldnewsforgreece;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {
    List<News> mArticles;
    MainActivity mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView section;
        protected TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            section = (TextView) itemView.findViewById(R.id.section);
        }
    }

    public NewsRecyclerAdapter(MainActivity context, List<News> articles) {
        this.mArticles = articles;
        this.mContext = context;
    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        ViewHolder vh = new ViewHolder(listItem);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position) {
        final News currentArticle = mArticles.get(position);
        holder.section.setText(currentArticle.getSection());
        holder.title.setText(currentArticle.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startWebView(currentArticle.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
