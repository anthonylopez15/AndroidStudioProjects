package com.exemple.android.noticiasdahora;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 10/01/2018.
 */

public final class QueryUtils {

    public static List<NoticiasBean> buscarDadosNoticias(String requestUrl) {
        URL url = criarUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = facaPedidoHttp(url);
        } catch (IOException e) {
            Log.e(QueryUtils.class.getSimpleName(), "Erro ao closing input stream", e);
        }
        List<NoticiasBean> noticiasList = extrairRecursosDoJson(jsonResponse);

        return noticiasList;
    }

    private static URL criarUrl(String requestUrl) {
        URL link = null;
        try {
            link = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return link;
    }

    private static String facaPedidoHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = lerDoStream(inputStream);
            } else {
                Log.e(QueryUtils.class.getSimpleName(), "Erro codigo de resposta: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(QueryUtils.class.getSimpleName(), "Problema retrieving the new JSON results.", e);
        } finally {
            urlConnection.disconnect();
            inputStream.close();
        }
        return jsonResponse;
    }

    private static String lerDoStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static ArrayList<NoticiasBean> extrairRecursosDoJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<NoticiasBean> noticiasList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject r = results.getJSONObject(i);
                String titulo = r.getString("webTitle");
                String sectionName = r.getString("sectionName");
                String data = r.getString("webPublicationDate");
                String url = r.getString("webUrl");

                noticiasList.add(new NoticiasBean(titulo, sectionName, url, data));
            }
        } catch (JSONException e) {
            Log.e(QueryUtils.class.getSimpleName(), "Problem parsing the new JSON results", e);
        }
        return noticiasList;
    }


}
