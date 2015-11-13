package com.vladbytsyuk.sebbiatesttask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class TweetsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Tweet> tweetsList;

    public TweetsAdapter(Context context, ArrayList<Tweet> tweetsList) {
        this.context = context;
        this.tweetsList = tweetsList;
    }

    @Override
    public int getCount() {
        return tweetsList.size();
    }

    @Override
    public Tweet getItem(int position) {
        return tweetsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            viewHolder = viewHolderInit(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tweetTimeTextView.setText(getItem(position).getTweetTime());
        viewHolder.tweetTextView.setText(getItem(position).getTweetText());
        return convertView;
    }

    private ViewHolder viewHolderInit(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tweetTimeTextView = (TextView) view.findViewById(R.id.tweetTimeTextView);
        viewHolder.tweetTextView = (TextView) view.findViewById(R.id.tweetTextView);
        return viewHolder;
    }
    
    static class ViewHolder {
        TextView tweetTimeTextView;
        TextView tweetTextView;
    }

}
