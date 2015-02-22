package com.harrisonmcguire.segify.MainPackage;

/**
 * Created by Harrison on 2/17/2015.
 */

import com.harrisonmcguire.segify.Adapter.RedditValueAdapter;
import com.harrisonmcguire.segify.VolleyPackage.VolleySingletonClass;
import com.harrisonmcguire.segify.Classes.RedditClass;
import com.harrisonmcguire.segify.R;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {

    // Reddit json urls
    private static final String subredditUrl = "http://www.reddit.com/r/";
    private static final String jsonEnd = "/.json";
    private static final String qCount = "?count=";
    private static final String after = "&after=";
    private static final String gamingUrlPlus = subredditUrl + "gaming" + jsonEnd;
    private static final String gamingUrl = "http://www.reddit.com/r/gaming/.json";

    private static final String sega = "sega";
    private static final String dreamcast = "dreamcast";
    private static final String saturn = "segasaturn";
    private static final String genesis = "segagenesis";
    private static final String sonic = "sonicthehedgehog";
    private static final String alien = "alienisolation";
    private static final String pso = "pso2";
    private static final String gameGear = "Game_Gear";
    private static final String jetSet = "jetsetradio";
    private static final String valkyria = "valkyria";

    private int counter = 0;
    private String count;
    private String jsonSubreddit;
    private String after_id;


    //declare the variables to handle json data
    private ListView listView;


    private List<RedditClass> redditList = new ArrayList<RedditClass>();
    private RedditValueAdapter adapter;

    //Loading box pop up
    private ProgressDialog pDialog;

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    //Create main activity and start app using the activity_main layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declare list view in activity-main for clickable rows
        ListView lv = (ListView) findViewById(R.id.list);

        // put Load More button at the bottom of the list view
        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        lv.addFooterView(footerView);

        // enable each row to be clickable and launch the intent
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String postUrl = ((TextView) view.findViewById(R.id.postUrl)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", postUrl);
                Log.d("the url", postUrl); // for debugging
                startActivity(intent);
            }
        });

        //Once the app launches, by default update list row with /r/sega/.json data
        updateList(sega);

        //add the load more button at the bottom of the list
        Button btnLoadMore = (Button) findViewById(R.id.load_more_button);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadMore(jsonSubreddit);
            }
        });
    }


    //updateList function using Volley to parse json data from the provided url
    public void updateList(String subreddit){

        // reset counter
        counter = 0;

        // set url for parsing
        subreddit = subredditUrl + subreddit + jsonEnd;

        //find the list view in activity_main with the id of list
        listView = (ListView) findViewById(R.id.list);

        //declare new adapter to handle the reddit values
        adapter = new RedditValueAdapter(this, redditList);

        //set the list view to the adapter
        listView.setAdapter(adapter);

        //use the clear adapter function to clear all current data so new data
        //can overtake the old data
        adapter.clearAdapter();

        //declare and show the loading box
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        //Parse JSON function
        //Establish the request cache to catch the json data using volley.jar
        JsonObjectRequest redditReq = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //print to logcat the entire json string for debugging
                Log.d(TAG, response.toString());
                hidePDialog();

                //try catch block to parse json
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    Log.d("after id", after_id);
                    JSONArray children = data.getJSONArray("children");

                    // Parsing json. The for loop will loop through array getting the data specified
                    // and catch the exceptions
                    for (int i = 0; i < children.length(); i++) {

                        JSONObject obj = children.getJSONObject(i).getJSONObject("data");
                        RedditClass post = new RedditClass();
                        post.setTitle(obj.getString("title"));
                        post.setThumbnailUrl(obj.getString("thumbnail"));
                        post.setDomain(obj.getString("domain"));
                        post.setAuthor(obj.getString("author"));
                        post.setSubreddit(obj.getString("subreddit"));
                        post.setPostUrl(obj.getString("url"));

                        jsonSubreddit = obj.getString("subreddit");

                        // adding posts to reddit array
                        redditList.add(post);

                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

                // update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        //Volley singleton
        VolleySingletonClass.getInstance().addToRequestQueue(redditReq);
    }

    // function for loadMore clickable button at the footer of the list
    public void loadMore(String subreddit){

        // variables for the next page subreddits
        counter =+ 25;
        count = String.valueOf(counter);
        subreddit = jsonSubreddit;

        // url for the nex page subreddit
        subreddit = subredditUrl + subreddit + jsonEnd + qCount + count + after + after_id;
        Log.d("urlSubreddit", subreddit); //for debugging

        // maintain position in the scroll view
        int index = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(counter);
        int top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());

        //find the list view in activity_main with the id of list
        listView = (ListView) findViewById(R.id.list);

        //set the list view to the adapter
        listView.setAdapter(adapter);

        //declare and show the loading box
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        //Parse JSON function
        //Establish the request cache to catch the json data using volley.jar
        JsonObjectRequest redditReq = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //print to logcat the entire json string for debugging
                Log.d(TAG, response.toString());
                hidePDialog();

                //try catch block to parse json
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    // Parsing json. The for loop will loop through array getting the data specified
                    // and catch the exceptions
                    for (int i = 0; i < children.length(); i++) {

                        JSONObject obj = children.getJSONObject(i).getJSONObject("data");
                        RedditClass post = new RedditClass();
                        post.setTitle(obj.getString("title"));
                        post.setThumbnailUrl(obj.getString("thumbnail"));
                        post.setDomain(obj.getString("domain"));
                        post.setAuthor(obj.getString("author"));
                        post.setSubreddit(obj.getString("subreddit"));
                        post.setPostUrl(obj.getString("url"));

                        // adding posts to reddit array
                        redditList.add(post);

                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

                // update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        // maintain scroll position
        listView.setSelectionFromTop(index, top);

        //Volley singleton
        VolleySingletonClass.getInstance().addToRequestQueue(redditReq);
    }

    //functions for each button press to update the list with the respected json data
    public void segaUpdate(View v) {
        updateList(sega);
    }

    public void dreamcastUpdate(View v) {
        updateList(dreamcast);
    }

    public void saturnUpdate(View v) {
        updateList(saturn);
    }

    public void genesisUpdate(View v) {
        updateList(genesis);
    }

    public void sonicUpdate(View v) {
        updateList(sonic);
    }

    public void alienUpdate(View v) {
        updateList(alien);
    }

    public void psoUpdate(View v) {
        updateList(pso);
    }

    public void gameGearUpdate(View v) {
        updateList(gameGear);
    }

    public void jetSetUpdate(View v) {
        updateList(jetSet);
    }

    public void valkyriaUpdate(View v) {
        updateList(valkyria);
    }


    //menu option if applicable
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // function to hide the loading dialog box
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    // Stop app from running
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

}