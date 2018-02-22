package com.exemple.android.noticiasdahora;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoticiasActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NoticiasBean>> {

    private NoticiasAdapter noticiasAdapter;

    private ProgressBar progressBar;

    private TextView stateTextView;

    private static final String GUARDIAN_URL_QUERY =
//            "http://content.guardianapis.com/world/brazil?api-key=test";
            "https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2018-01-01&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticias_activity);

        ListView noticiasListView = findViewById(R.id.list);

        noticiasAdapter = new NoticiasAdapter(this, new ArrayList<NoticiasBean>());

        noticiasListView.setAdapter(noticiasAdapter);

        stateTextView = findViewById(R.id.status_view);
        noticiasListView.setEmptyView(stateTextView);

        noticiasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NoticiasBean noticia = noticiasAdapter.getItem(i);
                Uri uri = Uri.parse(noticia.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this).forceLoad();
        }else{
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            stateTextView.setText(R.string.no_connection);
        }

    }

    @Override
    public Loader<List<NoticiasBean>> onCreateLoader(int i, Bundle bundle) {
        return new NoticiasLoader(NoticiasActivity.this, GUARDIAN_URL_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<List<NoticiasBean>> loader, List<NoticiasBean> noticiasBeans) {

        stateTextView.setText(R.string.no_empty_view);

        noticiasAdapter.clear();

        if (noticiasBeans != null && !noticiasBeans.isEmpty()) {
            noticiasAdapter.addAll(noticiasBeans);
        }

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NoticiasBean>> loader) {
        noticiasAdapter.clear();
    }
}
