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
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
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
    private static final String gamingUrlPlus = subredditUrl + "gaming" + jsonEnd;
    private static final String gamingUrl = "http://www.reddit.com/r/gaming/.json";
    private static final String segaUrl = "http://www.reddit.com/r/sega/.json";
    private static final String dreamcastUrl = "http://www.reddit.com/r/dreamcast/.json";
    private static final String saturnUrl = "http://www.reddit.com/r/segasaturn/.json";
    private static final String genesisUrl = "http://www.reddit.com/r/segagenesis/.json";
    private static final String sonicUrl = "http://www.reddit.com/r/sonicthehedgehog/.json";
    private static final String alienUrl = "http://www.reddit.com/r/alienisolation/.json";
    private static final String psoUrl = "http://www.reddit.com/r/pso2/.json";
    private static final String gameGearUrl = "http://www.reddit.com/r/Game_Gear/.json";
    private static final String jetSetUrl = "http://www.reddit.com/r/jetsetradio/.json";
    private static final String valkyriaUrl = "http://www.reddit.com/r/valkyria/.json";

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

        // declare list view in activity-main for parsed data
        ListView lv = (ListView) findViewById(R.id.list);

        // enable each row to be clickable and launch the intent
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String postUrl = ((TextView) view.findViewById(R.id.postUrl)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", postUrl);
                Log.d("THE URL IS", postUrl);
                startActivity(intent);
            }
        });

        //Once the app launches, by default update list row with /r/sega/.json data
        updateList(segaUrl);
    }


    //updateList function using Volley to parse json data from the provided url
    public void updateList(String urlSubreddit){

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
        JsonObjectRequest redditReq = new JsonObjectRequest(Request.Method.GET, urlSubreddit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //print to logcat the entire json string for debugging
                Log.d(TAG, response.toString());
                hidePDialog();

                //try catch block to parse json
                try {
                    JSONObject data = response.getJSONObject("data"); /* this is weird I have to check it >>> */
                    JSONArray children = data.getJSONArray("children");

                    // Parsing json. The for loop will loop through array getting the data specified
                    // and catch the exceptions
                    for (int i = 0; i < children.length(); i++) {

                        JSONObject obj = children.getJSONObject(i).getJSONObject("data");    //.getJSONObject("data");
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
        //Volley singleton
        VolleySingletonClass.getInstance().addToRequestQueue(redditReq);
    }

    //functions for each button press to update the list with the respected json data
    public void segaUpdate(View v) {
        updateList(segaUrl);
    }

    public void dreamcastUpdate(View v) {
        updateList(dreamcastUrl);
    }

    public void saturnUpdate(View v) {
        updateList(saturnUrl);
    }

    public void genesisUpdate(View v) {
        updateList(genesisUrl);
    }

    public void sonicUpdate(View v) {
        updateList(sonicUrl);
    }

    public void alienUpdate(View v) {
        updateList(alienUrl);
    }

    public void psoUpdate(View v) {
        updateList(psoUrl);
    }

    public void gameGearUpdate(View v) {
        updateList(gameGearUrl);
    }

    public void jetSetUpdate(View v) {
        updateList(jetSetUrl);
    }

    public void valkyriaUpdate(View v) {
        updateList(valkyriaUrl);
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