package com.exemple.android.noticiasdahora;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anthony on 09/01/2018.
 */

public class NoticiasAdapter extends ArrayAdapter<NoticiasBean> {


    private int corBackground = 0;

    public NoticiasAdapter(Activity context, ArrayList<NoticiasBean> noticias) {
        super(context, 0, noticias);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.noticias_list_item, parent, false);
        }

        NoticiasBean noticiasBean = getItem(position);

        TextView icone = listItemView.findViewById(R.id.letra);
        icone.setText(pegarPrimeroLetra(noticiasBean.getSectionName()));

        TextView titulo = listItemView.findViewById(R.id.titulo);
        titulo.setText(noticiasBean.getTitulo());

        TextView sectionName = listItemView.findViewById(R.id.section_name);
        sectionName.setText(noticiasBean.getSectionName());

        SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataParaConverter = noticiasBean.getData().replace("T", " ").replace("Z", "");
        Date dataConvertida = null;

        try {
            dataConvertida = formatador.parse(dataParaConverter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView data = listItemView.findViewById(R.id.data);
        data.setText(converterData(dataConvertida));

        TextView mHora = listItemView.findViewById(R.id.hora);
        mHora.setText(converterHora(dataConvertida));

        GradientDrawable iconColor = (GradientDrawable) icone.getBackground();
        int corIcone = pegarCorFundo(noticiasBean.getSectionName());
        iconColor.setColor(corIcone);

        return listItemView;
    }

    public String converterData(Date data) {
        SimpleDateFormat formatador = new SimpleDateFormat("MMM dd, yyyy");
        return formatador.format(data);
    }

    public String converterHora(Date data) {
        SimpleDateFormat formatador = new SimpleDateFormat("h:mm a");
        return formatador.format(data);
    }

    public String pegarPrimeroLetra(String texto) {
        return texto.substring(0, 1);
    }

    public int pegarCorFundo(String sectionName) {
        int colorResourceId;

        if (sectionName.equals("Politics")) {
            colorResourceId = R.color.magnitude1;
        } else if (sectionName.equals("UK news")) {
            colorResourceId = R.color.magnitude2;
        } else if (sectionName.equals("Opinion")) {
            colorResourceId = R.color.magnitude3;
        } else if (sectionName.equals("Society")) {
            colorResourceId = R.color.magnitude4;
        } else if (sectionName.equals("Environment")) {
            colorResourceId = R.color.magnitude5;
        } else if (sectionName.equals("Education")) {
            colorResourceId = R.color.magnitude6;
        }else if (sectionName.equals("Media")) {
            colorResourceId = R.color.magnitude7;
        }else {
            colorResourceId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), colorResourceId);
    }

}