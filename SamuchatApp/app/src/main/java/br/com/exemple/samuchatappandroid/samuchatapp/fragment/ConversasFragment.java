package br.com.exemple.samuchatappandroid.samuchatapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.exemple.samuchatappandroid.samuchatapp.R;
import br.com.exemple.samuchatappandroid.samuchatapp.activity.ConversaActivity;
import br.com.exemple.samuchatappandroid.samuchatapp.activity.LoginActivity;
import br.com.exemple.samuchatappandroid.samuchatapp.adapter.ConversaAdapter;
import br.com.exemple.samuchatappandroid.samuchatapp.config.ConfiguracaoFirebase;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Base64Custom;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Preferencias;
import br.com.exemple.samuchatappandroid.samuchatapp.modelo.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {


    private ListView listView;
    private ArrayAdapter<Conversa> adapter;
    private ArrayList<Conversa> conversas;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;

    private String idUsuarioLogadoFinal;
    private String idUsuarioLogadoBundle;

    static String TAG = "ConversasFragments";


    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        // Monta listview e adapter
        conversas = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        adapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(adapter);

        // recuperar dados do usuário
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogadoPreference = preferencias.getIdentificador();


        Bundle extra = getActivity().getIntent().getExtras();
        if(extra != null){
        idUsuarioLogadoBundle = extra.getString("idUsuarioLogado");
        }

        if (idUsuarioLogadoPreference == null) {
            idUsuarioLogadoFinal = idUsuarioLogadoBundle;
            Log.i(TAG, "Deu prefence nulo, idUsuarioLogadoFinal = " + idUsuarioLogadoFinal);
        } else {
            idUsuarioLogadoFinal = idUsuarioLogadoPreference;
            Log.i(TAG, "Deu prefence, idUsuarioLogadoFinal = " + idUsuarioLogadoFinal);
        }

        // Recuperar conversas do Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child(idUsuarioLogadoFinal);

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Adicionar evento de clique na lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Conversa conversa = conversas.get(position);
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                intent.putExtra("nome", conversa.getNome());
                String email = Base64Custom.decodificarBase64(conversa.getIdUsuario());
                intent.putExtra("email", email);

                startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }

}
