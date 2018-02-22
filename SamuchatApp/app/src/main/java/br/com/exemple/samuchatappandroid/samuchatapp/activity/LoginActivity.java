package br.com.exemple.samuchatappandroid.samuchatapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import br.com.exemple.samuchatappandroid.samuchatapp.R;
import br.com.exemple.samuchatappandroid.samuchatapp.config.ConfiguracaoFirebase;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Base64Custom;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Permissao;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Preferencias;
import br.com.exemple.samuchatappandroid.samuchatapp.modelo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Verifica se o usuário já está logado
        verificarUsuarioLogado();

        email = (EditText) findViewById(R.id.edit_login_email);
        senha = (EditText) findViewById(R.id.edit_login_senha);

        botaoLogar = (Button) findViewById(R.id.bt_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campos vazio", Toast.LENGTH_LONG).show();
                } else {
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    Toast.makeText(LoginActivity.this, " " + usuario.getEmail(), Toast.LENGTH_LONG).show();
                    validarLogin();
                }
            }
        });

    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);

    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());
                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificadorUsuarioLogado);
                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificadorUsuarioLogado, usuarioRecuperado.getNome());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);


                    abrirTelaPrincipal();
//                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
        }
    }

}