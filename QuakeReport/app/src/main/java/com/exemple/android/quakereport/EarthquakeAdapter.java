package com.exemple.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anthony on 14/11/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }
        Earthquake currentEarthquake = getItem(position);

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(formatMagnitude(currentEarthquake.getmMagnitude()));


        // Sete a cor de fundo apropriada no círculo de magnitude.
        // Busque o fundo do TextView, que é um GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Obtenha a cor de fundo apropriada baseada na magnitude do terremoto atual
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        // Sete a cor no círculo de magnitude
        magnitudeCircle.setColor(magnitudeColor);


        String originalLocation = currentEarthquake.getmLocation();
        String primaryLocation, locationOffset;

        if (originalLocation.contains("of")) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView locationOffsetView = listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        TextView primaryLocationView = listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        Date dateObject = new Date(currentEarthquake.getmTimeInMilliseconds());

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(formatDate(dateObject));

        // Acha a TextView com view ID de time
        TextView timeView = listItemView.findViewById(R.id.time);
        // Formata o tempo em string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Mostra o tempo do terremoto atual na TextView
        timeView.setText(formattedTime);

        return listItemView;
    }

    /**
     * Retorna a data string formatada (i.e. "Mar 3, 1984") de um objeto Date.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Retorna a data string formatada (i.e. "4:30 PM") de um objeto Date.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double mag) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(mag);
    }


    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        switch ((int) magnitude) {
            case 0:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
