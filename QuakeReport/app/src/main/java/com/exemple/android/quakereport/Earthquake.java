package com.exemple.android.quakereport;

/**
 * Created by Anthony on 14/11/2017.
 */

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    /**
     * Constrói um novo objeto {@link Earthquake}.
     *
     * @param magnitude          é a magnitude (tamanho) do terremoto
     * @param location           é a localização da cidade do earthquake
     * @param timeInMilliseconds é o tempo em mili-segundos (da época) quando o
     *                           terremoto aconteceu
     * @param url                é o URL do website para achar mais detalhes sobre o earthquake
     */
    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    /**
     * Retorna o URL do website para achar mais informações sobre o earthquake.
     */
    public String getUrl() {
        return mUrl;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

}
