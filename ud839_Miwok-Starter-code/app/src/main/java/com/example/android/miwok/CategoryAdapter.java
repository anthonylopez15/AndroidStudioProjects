package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Switch;

/**
 * Created by Anthony on 09/11/2017.
 */

/**
 * {@link CategoryAdapter} é um {@link FragmentPagerAdapter} que pode prover o layout para
 * cada item de lista baseado em um recurso de dado que é uma lista de {@link Word} objetos.
 */
public class CategoryAdapter extends FragmentPagerAdapter {
    //Contexto do aplicativo
    private Context mContext;

    /**
     * Cria um novo {@link CategoryAdapter} objeto.
     *
     * @param fm é o fragment manager que manterá cada estado de fragment no adapter
     *           ao longo de cada deslizada.
     */
    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Retorna o {@link Fragment} que deve ser mostrado para o dado número de página.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new FamilyFragment();
        } else if (position == 2) {
            return new ColorsFragment();
        } else {
            return new PhrasesFragment();
        }
    }

    /**
     * Retorna o número total de páginas.
     */
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_numbers);
        } else if (position == 1) {
            return mContext.getString(R.string.category_family);
        } else if (position == 2) {
            return mContext.getString(R.string.category_colors);
        } else {
            return mContext.getString(R.string.category_phrases);
        }
    }
}
