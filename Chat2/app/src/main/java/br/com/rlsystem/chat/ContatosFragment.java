package br.com.rlsystem.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.List;


public class ContatosFragment extends Fragment {

    ListView ltwContatos = null;
    List<Contato> lista = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contatos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        ltwContatos = (ListView) getView().findViewById(R.id.ltwContatos);
        refreshListView();

        ltwContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getActivity(), ChatActivity.class);
                it.putExtra("contact_user", lista.get(position).getContact_user());
                it.putExtra("name_contact", lista.get(position).getName_contact());
                it.putExtra("photo_contact", lista.get(position).getPhoto_contact());
                startActivity(it);
            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("CONTACT"));

        FloatingActionButton btnAdd = (FloatingActionButton) getView().findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Adicionar Contato");
                alert.setMessage("Digite o e-mail do contato");

                final EditText txtEmail = new EditText(getContext());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                txtEmail.setLayoutParams(lp);
                txtEmail.setTextColor(Color.parseColor("#000000"));
                alert.setView(txtEmail);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getActivity().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                        final String id_user = String.valueOf(preferences.getInt("id_usuario", 0));

                        String URL = "http://192.168.25.16/chat/add_contact.php";

                        Ion.with(getContext())
                                .load(URL)
                                .setMultipartParameter("email_user", txtEmail.getText().toString())
                                .setMultipartParameter("id_user", id_user)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        if (result.get("retorno").getAsString().equals("YES")) {
                                            Toast.makeText(getContext(), "Contato cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                                            SQLiteHelper db = new SQLiteHelper(getContext());
                                            db.save_contact(Integer.parseInt(id_user), result.get("contact_user").getAsInt(), result.get("nome_user").getAsString(), result.get("photo_user").getAsString());
                                            refreshListView();

                                        } else if (result.get("retorno").getAsString().equals("EMAIL_ERROR")) {
                                            Toast.makeText(getContext(), "E-mail não existe!", Toast.LENGTH_LONG).show();
                                        } else if (result.get("retorno").getAsString().equals("CONTACT_EXIST")) {
                                            Toast.makeText(getContext(), "Contato já cadastrado anteriormente!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                });

                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alert.show();
            }
        });

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshListView();
        }
    };

    private void refreshListView(){
        SQLiteHelper db = new SQLiteHelper(getContext());
        this.lista = db.getContacts();

        ltwContatos.setAdapter(new ContatosAdapter(getContext(), this.lista));
    }
}
