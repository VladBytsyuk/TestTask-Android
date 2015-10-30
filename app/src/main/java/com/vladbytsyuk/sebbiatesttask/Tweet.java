package com.vladbytsyuk.sebbiatesttask;


/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class Tweet {
    private String tweetTime;
    private String tweetText;

    public Tweet(String tweetTime, String tweetText) {
        this.tweetTime = tweetTime;
        this.tweetText = tweetText;
    }

    public String getTweetTime() {
        return tweetTime;
    }

    public void setTweetTime(String tweetTime) {
        this.tweetTime = tweetTime;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }
}
