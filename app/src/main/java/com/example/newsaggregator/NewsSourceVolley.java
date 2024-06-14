package com.example.newsaggregator;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NewsSourceVolley {

    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static Source source;

    private static final String sourceURL = "https://newsapi.org/v2/sources";

    private static final String newsAPIKey = "0e8d737087074938b5d37e0f2d45cba8";

    public static void downloadSource(MainActivity mainActivityIn){

        mainActivity = mainActivityIn;

        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(sourceURL).buildUpon();
        buildURL.appendQueryParameter("apiKey", newsAPIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.updatingSourcesFailed();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "");
                        return headers;
                    }
                };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    private static void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONArray sourceList = jObjMain.getJSONArray("sources");
            ArrayList<Source> sources = new ArrayList<>();
            Set<String> categories = new HashSet<>();

            for (int i = 0; i < sourceList.length(); i++) {
                JSONObject sourceObj = sourceList.getJSONObject(i);
                String category = sourceObj.getString("category");

                Source source = new Source(sourceObj.getString("id"), sourceObj.getString("name"), category);
                categories.add(category);

                sources.add(source);
            }
            mainActivity.updatingData(sources);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
