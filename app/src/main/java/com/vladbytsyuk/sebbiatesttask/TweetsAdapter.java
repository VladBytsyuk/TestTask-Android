package com.vladbytsyuk.sebbiatesttask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class TweetsAdapter extends BaseAdapter {
    private LayoutInflater tweetsAdapterInflater;
    private ArrayList<Tweet> tweetsList;

    public TweetsAdapter(Context context, ArrayList<Tweet> tweetsList) {
        this.tweetsAdapterInflater = LayoutInflater.from(context);
        this.tweetsList = tweetsList;
    }

    public TweetsAdapter(Context context, Tweet... tweets) {
        this.tweetsAdapterInflater = LayoutInflater.from(context);
        ArrayList<Tweet> buf = new ArrayList<>();
        for (Tweet x : tweets) {
            buf.add(x);
        }
        this.tweetsList = buf;
    }

    @Override
    public int getCount() {
        return tweetsList.size();
    }

    @Override
    public Object getItem(int position) {
        return tweetsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = tweetsList.get(position);
        convertView = tweetsAdapterInflater.inflate(R.layout.list_item, parent, false);
        ((TextView) convertView.findViewById(R.id.tweetTimeTextView)).setText(tweet.getTweetTime());
        ((TextView) convertView.findViewById(R.id.tweetTextView)).setText(tweet.getTweetText());
        return convertView;
    }
}
