package com.harrisonmcguire.segify.Classes;

/**
 * Created by Harrison on 2/17/2015.
 */

public class RedditClass {

    private String title, thumbnailUrl, author, domain, postUrl, subreddit;

    //public class to use in main activity
    public RedditClass() {
    }

    //class of values to keep all the relevant data
    public RedditClass(String title, String thumbnailUrl, String author, String domain, String subreddit, String postUrl) {
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.author = author;
        this.domain = domain;
        this.postUrl = postUrl;
        this.subreddit = subreddit;
    }

    //functions to set and get strings from the RedditClass
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

}
