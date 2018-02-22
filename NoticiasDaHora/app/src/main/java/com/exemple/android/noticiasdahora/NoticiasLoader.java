package com.exemple.android.noticiasdahora;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 10/01/2018.
 */

public class NoticiasLoader extends AsyncTaskLoader<List<NoticiasBean>> {

    private String mUrl;

    public NoticiasLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NoticiasBean> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<NoticiasBean> listaDeNoticias = QueryUtils.buscarDadosNoticias(mUrl);

        return listaDeNoticias;
    }
}
