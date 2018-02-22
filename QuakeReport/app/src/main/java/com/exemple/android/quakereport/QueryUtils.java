package com.exemple.android.quakereport;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.HttpAuthHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anthony on 27/11/2017.
 */
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

public final class QueryUtils {

    private QueryUtils(){ }

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonReponse = null;

        try {
            jsonReponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Erro ao closing input stream", e);
        }
        List<Earthquake> earthquakeList = extractFeatureFromJson(jsonReponse);

        return earthquakeList;
    }

    private static URL createUrl(String url) {
        URL link = null;
        try {
            link = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return link;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Earthquake> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray features = root.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject quake = features.getJSONObject(i);
                JSONObject properties = quake.getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(magnitude, location, time, url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

}
