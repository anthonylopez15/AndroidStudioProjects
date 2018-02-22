package com.exemple.android.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioReference = databaseReference.child("usuarios");
    private DatabaseReference produtoReference = databaseReference.child("produtos").child("001");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Usuario usuario = new Usuario();
        usuario.setNome("Rayssa");
        usuario.setSobrenome("Miguela");
        usuario.setSexo("Feminino");
        usuario.setIdade("21");

        Produto produto = new Produto();
        produto.setDescricao("Impressora");
        produto.setFabricante("Hp");
        produto.setCor("Preto");

        produtoReference.child("001").setValue(produto); */

        usuarioReference.addValueEventListener(new ValueEventListener() {//Listener para receber modificações automaticos do Firebase
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//será chamado sempre q os dados do DB forem modificados
                Log.i("FIREBASE", dataSnapshot.getValue().toString() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
