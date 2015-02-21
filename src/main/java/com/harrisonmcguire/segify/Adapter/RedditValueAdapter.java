package com.harrisonmcguire.segify.Adapter;

/**
 * Created by Harrison on 2/17/2015.
 */

import com.harrisonmcguire.segify.R;
import com.harrisonmcguire.segify.VolleyPackage.VolleySingletonClass;
import com.harrisonmcguire.segify.Classes.RedditClass;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class RedditValueAdapter extends BaseAdapter {

    //declare variables for adapter
    private Activity activity;
    private LayoutInflater inflater;
    private List<RedditClass> redditValues;

    // volley imageLoader will load urls into NetworkImageViews
    ImageLoader imageLoader = VolleySingletonClass.getInstance().getImageLoader();

    public RedditValueAdapter(Activity activity, List<RedditClass> redditValues) {
        this.activity = activity;
        this.redditValues = redditValues;
    }

    //get size of array class
    @Override
    public int getCount() {
        return redditValues.size();
    }

    //get the values in the class
    @Override
    public Object getItem(int location) {
        return redditValues.get(location);
    }

    //get the item ids in the class
    @Override
    public long getItemId(int position) {
        return position;
    }

    //function to clear adapter when updating the list view
    public void clearAdapter()
    {
        redditValues.clear();
        //selected.clear();
        notifyDataSetChanged();
    }

    //function populate rows in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //declare inflater
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //declare convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        //declare imageLoader
        if (imageLoader == null) {
            imageLoader = VolleySingletonClass.getInstance().getImageLoader();
        }

        //setup row items
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subreddit = (TextView) convertView.findViewById(R.id.subreddit);
        TextView author = (TextView) convertView.findViewById(R.id.author);
        TextView postUrl = (TextView) convertView.findViewById(R.id.postUrl);

        // getting reddit values for the row
        RedditClass m = redditValues.get(position);

        // set default image and thumbnail image for NetworkImageView
        thumbNail.setDefaultImageResId(R.drawable.reddit);
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // set reddit post title to text box
        title.setText(m.getTitle());

        // set subreddit and domain in the same text box
        subreddit.setText(m.getSubreddit() + "   " + m.getDomain());

        //set author text box
        author.setText(String.valueOf(m.getAuthor()));

        // set the url to the subreddit
        postUrl.setText(String.valueOf(m.getPostUrl()));

        return convertView;
    }

}
