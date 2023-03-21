package com.example.trackingapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetData extends AsyncTask {
    JSONArray data;
    @Override
    protected Object doInBackground(Object[] objects) {
        data = JavaRestClient.get_stats();
        return null;
    }
    public JSONArray getData(){
        return data;
    }
}