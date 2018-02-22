package com.exemple.android.noticiasdahora;

/**
 * Created by Anthony on 09/01/2018.
 */

public class NoticiasBean {

    private String titulo;
    private String sectionName;
    private String url;
    private String data;

    public NoticiasBean(String titulo, String sectionName, String url, String data) {
        this.titulo = titulo;
        this.sectionName = sectionName;
        this.url = url;
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getUrl() {
        return url;
    }

    public String getData() {
        return data;
    }

}
