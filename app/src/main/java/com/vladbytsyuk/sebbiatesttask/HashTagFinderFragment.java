package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    DBHelper dbHelper;
    SQLiteDatabase db;

    public interface OnItemPressed {
        void itemPressed(Tweet tweet);
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
                    listener.itemPressed(adapter.getItem(position));
                }
            }
        });
        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase(); // need to be closed



        ArrayList<Tweet> dowloadedTweets = new ArrayList<>();
        Cursor c = db.query("tweetsTable", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int nameCol = c.getColumnIndex("name");
            int tweetCol = c.getColumnIndex("tweet");
            int timeCol = c.getColumnIndex("time");
            int imgCol = c.getColumnIndex("img");
            int friendsCol = c.getColumnIndex("friends");

            do {
                String name = c.getString(nameCol);
                String tweet = c.getString(tweetCol);
                String time = c.getString(timeCol);
                String img = c.getString(imgCol);
                Integer friends = c.getInt(friendsCol);
                dowloadedTweets.add(new Tweet(time, tweet, name, img, friends));
            } while (c.moveToNext());
        }
        adapter = new TweetsAdapter(getActivity(), dowloadedTweets);
        listView.setAdapter(adapter);




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
        db.delete("tweetsTable", null, null);
        findHashTag(hashTagEditText.getText().toString());
    }

    private ArrayList<Tweet> getTweets(String hashTag) {
        ArrayList<Tweet> dowloadedTweets = new ArrayList<>();
        DownloadTweets downloadTweets = new DownloadTweets();
        downloadTweets.execute(hashTag);
        try {
            downloadTweets.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cursor c = db.query("tweetsTable", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int nameCol = c.getColumnIndex("name");
            int tweetCol = c.getColumnIndex("tweet");
            int timeCol = c.getColumnIndex("time");
            int imgCol = c.getColumnIndex("img");
            int friendsCol = c.getColumnIndex("friends");

            do {
                String name = c.getString(nameCol);
                String tweet = c.getString(tweetCol);
                String time = c.getString(timeCol);
                String img = c.getString(imgCol);
                Integer friends = c.getInt(friendsCol);
                dowloadedTweets.add(new Tweet(time, tweet, name, img, friends));
            } while (c.moveToNext());
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

    private class DownloadTweets extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
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

                ContentValues cv = new ContentValues();

                for (int i = 0; i < TWEETS_AMOUNT; ++i) {
                    JSONObject statusesObjectJSON = statusesJSON.getJSONObject(i);
                    String tweetTime = statusesObjectJSON.getString("created_at");
                    cv.put("time", tweetTime);

                    String tweetText = statusesObjectJSON.getString("text");
                    cv.put("tweet", tweetText);

                    JSONObject userJSON = statusesObjectJSON.getJSONObject("user");
                    String name = userJSON.getString("name");
                    cv.put("name", name);

                    String avatarUrl = userJSON.getString("profile_image_url_https");
                    cv.put("img", avatarUrl);

                    Integer friendsCount = userJSON.getInt("friends_count");
                    cv.put("friends", friendsCount);

                    db.insert("tweetsTable", null, cv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}