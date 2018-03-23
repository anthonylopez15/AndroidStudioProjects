package br.com.rlsystem.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatsAdapter extends BaseAdapter {

    private Context ctx = null;
    private List<Chat> lista = null;

    public ChatsAdapter(Context ctx, List<Chat> lista){
            this.ctx = ctx;
            this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Chat getItem(int position) {
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
            v = inflater.inflate(R.layout.model_all_chats, null);
        } else {
            v = convertView;
        }

        Chat c = getItem(position);
        SQLiteHelper db = new SQLiteHelper(ctx);

        SharedPreferences preferences = ctx.getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

        Contato contato = null;

        if (preferences.getInt("id_usuario", 0) == c.getId_user()){
                contato = db.getContato(c.getContact_user());
        } else {
                contato = db.getContato(c.getId_user());
        }

        TextView txvNome = (TextView)v.findViewById(R.id.txvNome);
        txvNome.setText(contato.getName_contact());

        ImageView imgvPhoto = (ImageView)v.findViewById(R.id.imgvPhoto);

        Picasso.with(ctx).load(contato.getPhoto_contact()).resize(200, 200)
                .centerCrop().into(imgvPhoto);

        TextView txvMensagem = (TextView)v.findViewById(R.id.txvMensagem);
        if (c.getMensagem().length() > 10) {
            txvMensagem.setText(c.getMensagem().substring(0, 10));
        } else {
            txvMensagem.setText(c.getMensagem().substring(0, c.getMensagem().length()));
        }

        return v;
    }

    public void refresh(List<Chat> lista){
        this.lista = lista;
        notifyDataSetChanged();
    }
}
