package com.exemple.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;

import static com.exemple.android.quakereport.QueryUtils.fetchEarthquakeData;

/**
 * Created by Anthony on 06/12/2017.
 */


public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /** URL da busca */
    private String mUrl;
    /** Tag para mensagens de log */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();


    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null) {
            return null;
        }
        // Realiza requisição de rede, decodifica a resposta, e extrai uma lista de earthquakes.
        return fetchEarthquakeData(mUrl);
    }
}
