package com.wipro.codingexercise;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wipro.Utility.ApplicationConstants;
import com.wipro.adapter.ListAdapter;
import com.wipro.model.ItemDetails;
import com.wipro.model.ResponseData;
import com.wipro.networkhandler.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class displays RecyclerView and populates JSON data coming from server
 * and Lazily loads images in listss
 *
 * @author Pravin Madavi
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    //Used for logging
    private static final String TAG = "MainActivity";

    //RecyclerView instance to create list
    private RecyclerView mRecyclerView;

    //Instance Custom RecyclerView.Adapter
    private ListAdapter mListAdapter;

    //List of data which needs to be populated in RecyclerView
    private List<ItemDetails> mItemDetailsList;

    //ActionBar instance to set title dynamically
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view which displays RecyclerView i.e. list
        setContentView(R.layout.activity_main);

        // get the instance of RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // makes the network call using Volley Library to get the JSON data from server
        makeNetworkRequest();
    }

    /**
     * This method which makes Volley network call.
     *
     * @return Nothing.
     */
    private void makeNetworkRequest() {
        //Checks network connectivity if online make Volley Network StringRequest
        if (isOnline()) {
            //Show the progress dialog
            final ProgressDialog progressDialog = ProgressDialog.show(this, ApplicationConstants.ALERT,
                    ApplicationConstants.PLEASE_WAIT, true);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, ApplicationConstants.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //dismiss the progressDialog on response from server
                            progressDialog.dismiss();

                            //Get the JSON data into model class object
                            ResponseData mResponseData = new Gson().fromJson(response, ResponseData.class);

                            //Get the Support Action Bar in order to set title, received from server in response
                            mActionBar = getSupportActionBar();
                            mActionBar.setTitle(mResponseData.getTitle());

                            //get the ListAdapter instance by providing context and List of rows
                            mListAdapter = new ListAdapter(MainActivity.this, mResponseData.getRows());

                            //Set the LinearLayoutManager as LayoutManager to RecyclerView
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                            //Set the mListAdapter created above
                            mRecyclerView.setAdapter(mListAdapter);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //dismiss the progressDialog
                    progressDialog.dismiss();

                    // show the error toast to user
                    showInternetConnectionNotAvailableAlertDialog(ApplicationConstants.ERROR_OCCURED);
                }
            });

            // Get the Singleton Instance of RequestQueue ad Add the request to the Queue.
            ConnectionManager.getInstance(this).add(stringRequest);
        } else {
            //Network/internet is not available so show the toast to user
            showInternetConnectionNotAvailableAlertDialog(ApplicationConstants.INTERNET_NOT_AVAILABLE);
        }


    }

    /**
     * This method checks the network/internet connectivity.
     *
     * @return boolean.
     */
    public boolean isOnline() {
        //instantiate boolean variable to return; default false
        boolean connected = false;
        try {
            //get ConnectivityManager to get NetworkInfo
            ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //get the network status
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            //log the error
            Log.v(TAG, e.toString());
        }
        //retrurn false if no network/internet
        return connected;
    }


    /**
     * This method shows the toast to user
     *
     * @param message String message to be passed to show in Toast
     * @return Nothing.
     */
    private void showInternetConnectionNotAvailableAlertDialog(String message) {

        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(ApplicationConstants.ALERT)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(ApplicationConstants.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        dialog.dismiss();
                    }
                }).create();
        // show it
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //if it refresh button clicked
        if (id == R.id.action_refresh) {
            //refresh the list by making network request to check if any new data is there
            makeNetworkRequest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
