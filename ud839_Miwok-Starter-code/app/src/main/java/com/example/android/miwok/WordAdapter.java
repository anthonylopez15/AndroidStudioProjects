package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Anthony on 20/10/2017.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorResource;

    /**
     *       * Crie um novo objeto {@link WordAdapter}.
     *       *
     *       * @param context é o contexto atual (isto é, Activity) que o adaptador está sendo criado em.
     *       * @param palavras é a lista de {@link Word} s para ser exibida.
     */
    public WordAdapter(Context context, ArrayList<Word> words, int mColorResource) {
        super(context, 0, words);
        this.mColorResource = mColorResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Verifique se uma view existente está sendo reutilizada, caso contrário, infle a vista
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Obter o {@link Word} objeto localizado nesta posição na lista
        Word currentWord = getItem(position);

        // Encontre o TextView no layout list_item.xml com o ID miwok_text_view.
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        // Obter a tradução Miwok do objeto currentWord e definir este texto em o Miwok TextView.
        miwokTextView.setText(currentWord.getMiwokTranslation());

        // Encontre o TextView no layout list_item.xml com o ID default_text_view.
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Obter a tradução padrão do objeto currentWord e definir este texto em o TextView padrão.
        defaultTextView.setText(currentWord.getDefaultTranslation());

        // Encontre o ImageView no layout list_item.xml com a imagem ID.
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        // Verifique se uma imagem é fornecida para esta palavra ou não
        if (currentWord.hasImage()) {
            // Se uma imagem estiver disponível, exiba a imagem fornecida com base no ID do recurso
            imageView.setImageResource(currentWord.getImageResourceId());
            // Verifique se a visualização está visível
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Caso contrário, esconda o ImageView (defina a visibilidade para GONE)
            imageView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResource);
        textContainer.setBackgroundColor(color);

        // Retorn todo o layout do item da lista (contendo 2 TextViews) para que ele possa ser mostrado em ListView
        return listItemView;
    }
}