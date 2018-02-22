package com.exemple.android.autenticacaousuario;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signOut();

        if (firebaseAuth.getCurrentUser() != null) {// método retorna usuario atual ou nulo
            Log.i("VERIFICA USUARIO: ", "Usuario está logado");
        }else {
            Log.i("VERIFICA USUARIO: ", "Usuario nao esta logado");
        }
        /* serve pra fazer login o usuario
        firebaseAuth.signInWithEmailAndPassword("tony.lopez.15@gmail.com", "anthony123")//
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Sigin: ", "Sucesso ao logar no sistemas");
                        } else {
                            Log.i("Sigin: ", "ERRO ao logar no sistemas");
                        }
                    }
                });/*

        /* Cadastro de usuario
        firebaseAuth.createUserWithEmailAndPassword("tony.lopez.15@gmail.com", "anthony123")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){// se for sucesso
                            Log.i("createUser: ", "Sucesso ao criar usuario ");
                        }else {
                            Log.i("createUser: ", "ERRO ao CADASTRAR usuario ");
                        }
                    }
                });*/
    }
}
