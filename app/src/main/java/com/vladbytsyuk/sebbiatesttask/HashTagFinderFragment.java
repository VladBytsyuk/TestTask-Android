package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class HashTagFinderFragment extends Fragment {
    TweetsAdapter adapter;
    EditText hashTagEditText;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hash_tag_finder, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        hashTagEditText = (EditText) rootView.findViewById(R.id.hashTagEditText);
        adapter = new TweetsAdapter(getActivity(), getTweets("hello"));
        listView.setAdapter(adapter);
        return rootView;
    }

    private ArrayList<Tweet> getTweets(String hashTag) {
        ArrayList<Tweet> result = new ArrayList<>();
        DownloadTweets downloadTweets = new DownloadTweets();
        downloadTweets.execute(hashTag);
        try {
            result = downloadTweets.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void findHashTag() {
        adapter = new TweetsAdapter(getActivity(), getTweets(hashTagEditText.getText().toString()));
        listView.setAdapter(adapter);
    }

    private class DownloadTweets extends AsyncTask<String, Void, ArrayList<Tweet>> {
        @Override
        protected ArrayList<Tweet> doInBackground(String... params) {
            ArrayList<Tweet> result = new ArrayList<>();
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}