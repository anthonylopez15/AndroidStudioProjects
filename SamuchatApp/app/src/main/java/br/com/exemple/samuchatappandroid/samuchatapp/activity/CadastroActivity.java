package br.com.exemple.samuchatappandroid.samuchatapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.exemple.samuchatappandroid.samuchatapp.R;
import br.com.exemple.samuchatappandroid.samuchatapp.config.ConfiguracaoFirebase;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Base64Custom;
import br.com.exemple.samuchatappandroid.samuchatapp.helper.Preferencias;
import br.com.exemple.samuchatappandroid.samuchatapp.modelo.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.edit_cadastro_nome);
        email = (EditText) findViewById(R.id.edit_cadastro_email);
        senha = (EditText) findViewById(R.id.edit_cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.bt_cadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setNome( nome.getText().toString() );
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                cadastrarUsuario();

            }
        });

    }

    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){

                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG ).show();

                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setId( identificadorUsuario );
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarDados( identificadorUsuario, usuario.getNome() );

                    abrirLoginUsuario();

                }else{

                    String erro = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Escolha uma senha que contenha, letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email indicado não é válido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Já existe uma conta com esse e-mail.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário: " + erro, Toast.LENGTH_LONG ).show();
                }

            }
        });

    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
