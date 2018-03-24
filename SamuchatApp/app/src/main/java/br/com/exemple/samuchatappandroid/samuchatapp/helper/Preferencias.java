package br.com.exemple.samuchatappandroid.samuchatapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Anthony on 19/02/2018.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private static final String NOME_ARQUIVO = "chatapp.preferencias";
    private static  final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias( Context contextoParametro){
        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, Context.MODE_PRIVATE );
        editor = preferences.edit();
    }

    public void salvarDados( String identificadorUsuario, String nomeUsuario ){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        String idIdentificador = preferences.getString(CHAVE_IDENTIFICADOR, null);
        return idIdentificador;
    }
    public String getNome(){
        String nomeUsuario = preferences.getString(CHAVE_NOME, null);
        return nomeUsuario;
    }

}
