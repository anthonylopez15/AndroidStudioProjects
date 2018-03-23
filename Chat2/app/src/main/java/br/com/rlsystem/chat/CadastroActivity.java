package br.com.rlsystem.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CadastroActivity extends AppCompatActivity {

    private static final int REQUEST_DIALOG_PHOTO = 1;
    private int havePhoto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = (Button)findViewById(R.id.btnCadastrar);
        final ImageView imgFoto = (ImageView)findViewById(R.id.imgFoto);


        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFoto.setImageResource(android.R.color.transparent);
                Intent camera = ImagePicker.getPickImageIntent(getBaseContext());
                startActivityForResult(camera, REQUEST_DIALOG_PHOTO);
            }
        });

        btn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                EditText txtNome = (EditText)findViewById(R.id.txtNome);
                final EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
                EditText txtSenha = (EditText)findViewById(R.id.txtSenha);
                EditText txtSenha2 = (EditText)findViewById(R.id.txtSenha2);

                int error = 0;

                if (txtNome.getText().toString().equals("")){
                    txtNome.setError("Preencha o campo nome.");
                    txtNome.requestFocus();
                    error = 1;
                } else if (txtEmail.getText().toString().equals("")){
                    txtEmail.setError("Preencha o campo e-mail.");
                    txtEmail.requestFocus();
                    error = 1;
                } else if (txtSenha.getText().toString().equals("")){
                    txtSenha.setError("Preencha o campo senha.");
                    txtSenha.requestFocus();
                    error = 1;
                } else if (txtSenha2.getText().toString().equals("")){
                    txtSenha2.setError("Preencha o campo senha2.");
                    txtSenha2.requestFocus();
                    error = 1;
                } else if (!txtSenha.getText().toString().equals(txtSenha2.getText().toString())){
                    txtSenha2.setError("O campo senha 2 deve ser igual ao senha 1.");
                    txtSenha2.requestFocus();
                    error = 1;
                } else if (havePhoto == 0){
                    Toast.makeText(getBaseContext(), "Por favor, selecione uma foto", Toast.LENGTH_LONG).show();
                    error = 1;
                }

                if (error == 0) {
                    String URL = "http://192.168.25.16/chat/insert_user.php";

                    String photoFile = "";

                    try {
                        photoFile = getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).applicationInfo.dataDir + "//photo//perfil.png";
                    } catch (PackageManager.NameNotFoundException e) {

                    }

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setMultipartParameter("nome_user", txtNome.getText().toString())
                            .setMultipartParameter("email_user", txtEmail.getText().toString())
                            .setMultipartParameter("senha_user", txtSenha.getText().toString())
                            .setMultipartFile("photo_user", new File(photoFile))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsString().equals("YES")) {
                                        Toast.makeText(getBaseContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                                    } else if (result.get("retorno").getAsString().equals("EMAIL_ERROR")) {
                                        //Toast.makeText(getBaseContext(), "E-mail já existe", Toast.LENGTH_LONG).show();
                                        txtEmail.setError("E-mail já existe!");
                                        txtEmail.requestFocus();
                                    }
                                }
                            });
                    }
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_DIALOG_PHOTO){
            if (resultCode == Activity.RESULT_OK){
                Bitmap photoUser = ImagePicker.getImageFromResult(getBaseContext(), resultCode, data);
                ImageView imgFoto = (ImageView)findViewById(R.id.imgFoto);
                imgFoto.setImageBitmap(photoUser);
                havePhoto = 1;

                // Grava foto pasta
                File diretorio = Environment.getDataDirectory();
                String path = "//data//" + getBaseContext().getPackageName() + "//photo//";

                diretorio = new File(diretorio, path);
                diretorio.mkdirs();

                OutputStream out = null;

                File outputFile = new File(diretorio, "perfil.png");

                try {
                    out = new FileOutputStream(outputFile);
                    photoUser.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {

                }

            } else {
                Toast.makeText(getBaseContext(), "Por favor, selecione uma foto", Toast.LENGTH_LONG).show();
            }
        }
    }
}
