package br.com.rlsystem.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShowChat();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(it);
            }
        });


        Button BtnLogar = (Button)findViewById(R.id.BtnLogar);

        BtnLogar.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                EditText txtLoginEmail = (EditText)findViewById(R.id.txtLoginEmail);
                EditText txtLoginSenha = (EditText)findViewById(R.id.txtLoginSenha);

                int error = 0;

                if (txtLoginEmail.getText().toString().equals("")){
                    txtLoginEmail.setError("Preencha o campo e-mail.");
                    txtLoginEmail.requestFocus();
                    error = 1;
                } else if (txtLoginSenha.getText().toString().equals("")){
                    txtLoginSenha.setError("Preencha o campo senha.");
                    txtLoginSenha.requestFocus();
                    error = 1;
                }

                if (error == 0) {
                    String URL = "http://192.168.25.16/chat/login_user.php";

                    SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                    String token_user = preferences.getString("token_usuario", "");

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setBodyParameter("email_user", txtLoginEmail.getText().toString())
                            .setBodyParameter("senha_user", txtLoginSenha.getText().toString())
                            .setBodyParameter("token_user", token_user)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsInt() > 0) {

                                        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
                                        preferences.putInt("id_usuario", result.get("retorno").getAsInt());
                                        preferences.putString("nome_usuario", result.get("nome_usuario").getAsString());
                                        preferences.putString("email_usuario", result.get("email_usuario").getAsString());
                                        preferences.putString("photo_usuario", result.get("photo_usuario").getAsString());
                                        preferences.commit();

                                        // Grava Contatos Novos
                                        SaveContact(result.get("retorno").getAsInt());

                                        // Abrir a Tela Principal
                                        ShowChat();

                                    } else {
                                        Toast.makeText(getBaseContext(), "Erro", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void ShowChat(){
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);

        if (preferences.getInt("id_usuario", 0) > 0) {
            Intent it = new Intent(MainActivity.this, ChatsActivity.class);
            startActivity(it);
            finish();
        }
    }

    private void SaveContact(final int id_user){

        String URL = "http://192.168.25.16/chat/get_all_contacts.php";

        Ion.with(getBaseContext())
                .load(URL)
                .setBodyParameter("id_user", String.valueOf(id_user))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for (int i = 0; i < result.size(); i++){
                            JsonObject obj = result.get(i).getAsJsonObject();

                            SQLiteHelper db = new SQLiteHelper(getBaseContext());
                            db.save_contact(id_user, Integer.parseInt(obj.get("id_usuario").getAsString()), obj.get("nome_usuario").getAsString(), obj.get("photo_usuario").getAsString());
                        }
                    }
                });
    }
}
