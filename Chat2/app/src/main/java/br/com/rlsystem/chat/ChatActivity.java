package br.com.rlsystem.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    List<Chat> lista = null;
    ListView ltwChats = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Intent it = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Picasso.with(getBaseContext()).load(it.getStringExtra("photo_contact")).resize(200, 200)
                .centerCrop().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                getSupportActionBar().setLogo(d);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        getSupportActionBar().setTitle(it.getStringExtra("name_contact"));

        ltwChats = (ListView)findViewById(R.id.ltwChats);

        refreshListView();

        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                final String id_user = String.valueOf(preferences.getInt("id_usuario", 0));

                String URL = "http://192.168.25.16/chat/send_message.php";

                final EditText txtMensagem = (EditText)findViewById(R.id.txtMensagem);

                if (txtMensagem.getText().length() > 0) {

                    final SQLiteHelper db = new SQLiteHelper(getBaseContext());
                    final long id_reg = db.insert_message(Integer.parseInt(id_user), it.getIntExtra("contact_user", 0), txtMensagem.getText().toString(), "");
                    String txtMsg = txtMensagem.getText().toString();
                    txtMensagem.setText("");
                    refreshListView();

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setMultipartParameter("contact_user", String.valueOf(it.getIntExtra("contact_user", 0)))
                            .setMultipartParameter("id_user", id_user)
                            .setMultipartParameter("message", txtMsg)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsString().equals("YES")) {
                                        db.update_message(id_reg, result.get("data_message").getAsString());
                                        refreshListView();
                                    } else if (result.get("retorno").getAsString().equals("NO")) {

                                    } else if (result.get("retorno").getAsString().equals("CONTACT_NOT_EXIST")) {

                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("REFRESH"));
    }

    @Override
    protected  void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListView();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        } else {
            return false;
        }
    }

    private void refreshListView(){
        Intent it = getIntent();

        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

        SQLiteHelper db = new SQLiteHelper(getBaseContext());
        this.lista = db.getMessages(it.getIntExtra("contact_user", 0), preferences.getInt("id_usuario", 0));

        ltwChats.setAdapter(new ChatAdapter(ChatActivity.this, lista));
        ltwChats.setSelection(ltwChats.getCount() -1);
    }
}
