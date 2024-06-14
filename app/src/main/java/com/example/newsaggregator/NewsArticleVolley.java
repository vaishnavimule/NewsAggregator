package com.example.newsaggregator;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewsArticleVolley {
    private static String idSource;
    private static MainActivity mainActivity;
    private static RequestQueue queue;


    private static final String storyURL = "https://newsapi.org/v2/top-headlines";

    private static final String newsAPIKey = "0e8d737087074938b5d37e0f2d45cba8";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadStory(MainActivity mainActivityIn, String sourceId){
        mainActivity = mainActivityIn;
        idSource = sourceId;
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(storyURL).buildUpon();
        buildURL.appendQueryParameter("sources",idSource);
        buildURL.appendQueryParameter("apiKey", newsAPIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.fetchingArticlesFailed();

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void parseJSON(String s) {
        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONArray articleList = jObjMain.getJSONArray("articles");
            ArrayList<Story> Stories = new ArrayList<>();

            for (int i = 0; i < articleList.length(); i++) {
                JSONObject article = articleList.getJSONObject(i);

                String publishedAtStr = article.has("publishedAt") && !article.isNull("publishedAt") ? article
                        .getString("publishedAt") : null;

                Story story =
                        new Story(getStringField(article, "author"), getStringField(article, "title"),
                                getStringField(article, "description"), getStringField(article, "url"),
                                getStringField(article, "urlToImage"),
                                parseDate(publishedAtStr));
                Stories.add(story);
            }
            mainActivity.fetchingArticlesSuccess(Stories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getStringField(JSONObject obj, String field) throws JSONException {
        return obj.has(field) && !obj.isNull(field) ? obj.getString(field) : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime parseDate(String date) {
        if (date != null) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    // date/time
                    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    // offset (hh:mm - "+00:00" when it's zero)
                    .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
                    // offset (hhmm - "+0000" when it's zero)
                    .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
                    // offset (hh - "Z" when it's zero)
                    .optionalStart().appendOffset("+HH", "Z").optionalEnd()
                    // create formatter
                    .toFormatter();
            return !date.equals("null") ? LocalDateTime.parse(date, formatter) : null;
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatDateTime(LocalDateTime ldt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm", Locale.getDefault());
        return ldt.format(dtf);
    }
}
