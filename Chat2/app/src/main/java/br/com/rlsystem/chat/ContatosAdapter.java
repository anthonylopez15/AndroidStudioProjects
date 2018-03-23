package br.com.rlsystem.chat;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ContatosAdapter extends BaseAdapter {

    private Context ctx = null;
    private List<Contato> lista = null;

    public ContatosAdapter(Context ctx, List<Contato> lista){
            this.ctx = ctx;
            this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Contato getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
            v = inflater.inflate(R.layout.model_contacts, null);
        } else {
            v = convertView;
        }

        Contato c = getItem(position);

        TextView txvNome = (TextView)v.findViewById(R.id.txvNome);
        txvNome.setText(c.getName_contact());

        ImageView imgvPhoto = (ImageView)v.findViewById(R.id.imgvPhoto);
        Picasso.with(ctx).load(c.getPhoto_contact()).resize(200, 200)
                .centerCrop().into(imgvPhoto);

        return v;
    }
}
