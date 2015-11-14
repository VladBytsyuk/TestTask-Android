package com.vladbytsyuk.sebbiatesttask;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
public class HashTagFinderFragment extends Fragment  {
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

    public void setOnItemClickListener(OnItemPressed listener) {
        this.listener = listener;
    }

    public void removeOnItemClickListener() {
        this.listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hash_tag_finder, container, false);

        hashTagEditText = (EditText) rootView.findViewById(R.id.editTextHashTag);

        findButton = (Button) rootView.findViewById(R.id.buttonFind);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("tweetsTable", null, null);
                downloadTweetsByHashTag(hashTagEditText.getText().toString());
            }
        });

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.itemPressed(adapter.getItem(position));
                }
            }
        });

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarHashTagFinder);

        dbHelper = new DBHelper(getActivity());
        db = dbHelper.getWritableDatabase();

        adapter = new TweetsAdapter(getActivity(), getTweetsList());
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        db.close();
        dbHelper.close();
        super.onDestroyView();
    }


    private void downloadTweetsByHashTag(String hashTag) {
        if (hashTag != null) {
            DownloadTweets downloadTweets = new DownloadTweets();
            downloadTweets.execute(hashTag);
        }
    }

    private ArrayList<Tweet> getTweetsList() {
        ArrayList<Tweet> tweetsList = new ArrayList<>();
        Cursor c = db.query("tweetsTable", null, null, null, null, null, null);
        if(c.moveToFirst()) {
            int userNameColumnIndex = c.getColumnIndex("user_name");
            int tweetTextColumnIndex = c.getColumnIndex("tweet_text");
            int tweetTimeColumnIndex = c.getColumnIndex("tweet_time");
            int avatarUrlColumnIndex = c.getColumnIndex("avatar_url");
            int friendsCountColumnIndex = c.getColumnIndex("friends_count");
            do {
                String userName = c.getString(userNameColumnIndex);
                String tweetText = c.getString(tweetTextColumnIndex);
                String tweetTime = c.getString(tweetTimeColumnIndex);
                String avatarUrl = c.getString(avatarUrlColumnIndex);
                Integer friendsCount = c.getInt(friendsCountColumnIndex);
                tweetsList.add(new Tweet(userName, tweetText, tweetTime, avatarUrl, friendsCount));
            } while (c.moveToNext());
        }
        return tweetsList;
    }

    private class DownloadTweets extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new TweetsAdapter(getActivity(), getTweetsList());
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(String... params) {
            Integer TWEETS_AMOUNT = 100;
            String jsonFile = downloadJSONFile(params[0], TWEETS_AMOUNT);
            parseJSONFile(jsonFile, TWEETS_AMOUNT);
            return null;
        }

        private String downloadJSONFile(String hashTag, Integer tweetsAmount) {
            try {
                String BEARER_TOKEN = "Bearer AAAAAAAAAAAAAAAAAAAAADiJRQAAAAAAt%2Brjl%2Bqmz0rcy%2BBbuXBBsrUHGEg%3Dq0EK2aWqQMb15gCZNwZo9yqae0hpe2FDsS92WAu0g";
                URL url = new URL("https://api.twitter.com/1.1/search/tweets.json?&q=%23" + hashTag + "&count=" + tweetsAmount.toString());
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
                reader.close();
                connection.disconnect();
                return builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private void parseJSONFile(String jsonFile, Integer tweetsAmount) {
            try {
                JSONObject jsonFileJSON = new JSONObject(jsonFile);
                JSONArray statusesJSON = jsonFileJSON.getJSONArray("statuses");

                ContentValues cv = new ContentValues();

                for (int i = 0; i < tweetsAmount; ++i) {
                    JSONObject statusesObjectJSON = statusesJSON.getJSONObject(i);

                    JSONObject userJSON = statusesObjectJSON.getJSONObject("user");
                    String name = userJSON.getString("name");
                    cv.put("user_name", name);

                    String tweetText = statusesObjectJSON.getString("text");
                    cv.put("tweet_text", tweetText);

                    String tweetTime = statusesObjectJSON.getString("created_at");
                    cv.put("tweet_time", tweetTime);

                    String avatarUrl = userJSON.getString("profile_image_url_https");
                    cv.put("avatar_url", avatarUrl);

                    Integer friendsCount = userJSON.getInt("friends_count");
                    cv.put("friends_count", friendsCount);

                    db.insert("tweetsTable", null, cv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}