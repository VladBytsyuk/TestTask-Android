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
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
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
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hash_tag_finder, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
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
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Tweet> doInBackground(String... params) {
            ArrayList<Tweet> result = new ArrayList<>();
            Integer TWEETS_AMOUNT = 10;
            try {
                String BEARER_TOKEN = "Bearer AAAAAAAAAAAAAAAAAAAAADiJRQAAAAAAt%2Brjl%2Bqmz0rcy%2BBbuXBBsrUHGEg%3Dq0EK2aWqQMb15gCZNwZo9yqae0hpe2FDsS92WAu0g";
                URL url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=" + params[0] + "&result_type=mixed&count=" + TWEETS_AMOUNT.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", BEARER_TOKEN);
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000 /* milliseconds */);
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                String jsonFile  = builder.toString();
                JSONObject jsonFileJSON = new JSONObject(jsonFile);
                JSONArray statusesJSON = jsonFileJSON.getJSONArray("statuses");
                for (int i = 0; i < TWEETS_AMOUNT; ++i) {
                    JSONObject statusesObjectJSON = statusesJSON.getJSONObject(i);
                    String tweetTime = statusesObjectJSON.getString("created_at");
                    String tweetText = statusesObjectJSON.getString("text");
                    JSONObject userJSON = statusesObjectJSON.getJSONObject("user");
                    String name = userJSON.getString("name");
                    String avatarUrl = userJSON.getString("profile_image_url");
                    Integer friendsCount = userJSON.getInt("friends_count");
                    result.add(new Tweet(tweetTime, tweetText, name, avatarUrl, friendsCount));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Tweet> tweets) {
            super.onPostExecute(tweets);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}