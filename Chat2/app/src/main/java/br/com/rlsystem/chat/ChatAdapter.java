package br.com.rlsystem.chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private Context ctx = null;
    private List<Chat> lista = null;

    public ChatAdapter(Context ctx, List<Chat> lista){
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
            v = inflater.inflate(R.layout.model_chats, null);
        } else {
            v = convertView;
        }

        Chat c = getItem(position);

        TextView txvMensagem = (TextView)v.findViewById(R.id.txvMensagem);
        txvMensagem.setText(c.getMensagem());

        TextView txvData = (TextView)v.findViewById(R.id.txvData);


        if (c.getData().length() == 0){
            txvData.setText("Enviando ...");
        } else {
            String data = c.getData();
            Calendar c1 = Calendar.getInstance();
            if ((c1.get(Calendar.YEAR) == Integer
                    .parseInt(data.split("\\-")[0]))
                    && ((c1.get(Calendar.MONTH) + 1) == Integer.parseInt(data
                    .split("\\-")[1]))
                    && (c1.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(data
                    .split("\\-")[2].substring(0, 2)))) {
                txvData.setText(data.split("\\ ")[1]);
            } else {
                String new_data_br = data.split("\\-")[2].substring(0, 2)+"/"+ data.split("\\-")[1]+"/"+data.split("\\-")[0]+" " + data.split("\\-")[2].substring(2);
                txvData.setText(new_data_br);
            }
        }

        LinearLayout containerChat = (LinearLayout)v.findViewById(R.id.containerChat);
        LinearLayout containerChat2 = (LinearLayout)v.findViewById(R.id.containerChat2);

        SharedPreferences preferences = ((Activity)ctx).getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        final int id_user = preferences.getInt("id_usuario", 0);

        if (id_user == c.getId_user()){
            txvMensagem.setBackgroundResource(R.drawable.bubble_green);
            containerChat.setGravity(Gravity.RIGHT);
            containerChat2.setGravity(Gravity.RIGHT);
        } else {
            txvMensagem.setBackgroundResource(R.drawable.bubble_yellow);
            containerChat.setGravity(Gravity.LEFT);
            containerChat2.setGravity(Gravity.LEFT);
        }

        return v;
    }
}
