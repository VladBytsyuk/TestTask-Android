package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class HashTagFinderFragment extends Fragment implements View.OnClickListener {
    TweetsAdapter adapter;
    EditText hashTagEditText;
    Button findButton;
    ListView listView;
    ProgressBar progressBar;
    ArrayList<Tweet> dowloadedTweets;

    public interface OnItemPressed {
        void itemPressed(String avatarUrl, String name, Integer friendsCount);
    }

    private OnItemPressed listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hash_tag_finder, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.itemPressed(dowloadedTweets.get(position).getAvatarUrl(), dowloadedTweets.get(position).getName(), dowloadedTweets.get(position).getFriendsCount());
                }
            }
        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        findButton = (Button) rootView.findViewById(R.id.findButton);
        findButton.setOnClickListener(this);

        hashTagEditText = (EditText) rootView.findViewById(R.id.hashTagEditText);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        findHashTag(hashTagEditText.getText().toString());
    }

    private ArrayList<Tweet> getTweets(String hashTag) {
        DownloadTweets downloadTweets = new DownloadTweets();
        downloadTweets.execute(hashTag);
        try {
            dowloadedTweets = downloadTweets.get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);
        return dowloadedTweets;
    }

    public void findHashTag(String hashTag) {
        if (hashTag != null) {
            adapter = new TweetsAdapter(getActivity(), getTweets(hashTag));
            listView.setAdapter(adapter);
        }
    }

    public void setOnItemClickListener(OnItemPressed listener) {
        this.listener = listener;
    }

    public void removeOnItemClickListener() {
        this.listener = null;
    }

    private class DownloadTweets extends AsyncTask<String, Void, ArrayList<Tweet>> {
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
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                String jsonFile = builder.toString();
                JSONObject jsonFileJSON = new JSONObject(jsonFile);
                JSONArray statusesJSON = jsonFileJSON.getJSONArray("statuses");
                for (int i = 0; i < TWEETS_AMOUNT; ++i) {
                    JSONObject statusesObjectJSON = statusesJSON.getJSONObject(i);
                    String tweetTime = statusesObjectJSON.getString("created_at");
                    String tweetText = statusesObjectJSON.getString("text");
                    JSONObject userJSON = statusesObjectJSON.getJSONObject("user");
                    String name = userJSON.getString("name");
                    String avatarUrl = userJSON.getString("profile_image_url_https");
                    Integer friendsCount = userJSON.getInt("friends_count");
                    result.add(new Tweet(tweetTime, tweetText, name, avatarUrl, friendsCount));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

    }
}