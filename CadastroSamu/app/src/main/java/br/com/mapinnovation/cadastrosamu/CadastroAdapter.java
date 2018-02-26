package br.com.mapinnovation.cadastrosamu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

/**
 * Created by Anthony on 25/02/2018.
 */


public class CadastroAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public CadastroAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new CodidoFragment();
        } else if (position == 1) {
            return new NomeFragment();
        } else {
            return new OutrosFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}