package com.vladbytsyuk.sebbiatesttask;


/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class Tweet {
    private String tweetTime;
    private String tweetText;
    private String name;
    private String avatarUrl;
    private Integer friendsCount;

    public Tweet(String tweetTime, String tweetText, String name, String avatarUrl, Integer friendsCount) {
        this.tweetTime = tweetTime;
        this.tweetText = tweetText;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.friendsCount = friendsCount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }
}
