package com.vladbytsyuk.sebbiatesttask;


/**
 * Created by VladBytsyuk on 30.10.2015.
 */
public class Tweet {
    private String userName;
    private String tweetText;
    private String tweetTime;
    private String avatarUrl;
    private Integer friendsCount;

    public Tweet(String userName, String tweetText, String tweetTime, String avatarUrl, Integer friendsCount) {
        this.tweetTime = tweetTime;
        this.tweetText = tweetText;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.friendsCount = friendsCount;
    }

    public String getUserName() {
        return userName;
    }

    public String getTweetText() {
        return tweetText;
    }

    public String getTweetTime() {
        return tweetTime;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

}
