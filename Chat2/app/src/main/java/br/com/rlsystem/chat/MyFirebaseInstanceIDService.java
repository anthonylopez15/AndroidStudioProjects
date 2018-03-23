package br.com.rlsystem.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
        saveInPreference(refreshedToken);

        Log.d("MyFirebaseIIDService", refreshedToken);
    }

    private void saveInPreference(String token){
        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
        preferences.putString("token_usuario", token);
        preferences.commit();
    }

    private void sendRegistrationToServer(String token) {
        String URL = "http://192.168.25.16/chat/update_token.php";

        SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        final String id_user = String.valueOf(preferences.getInt("id_usuario", 0));

        if (preferences.getInt("id_usuario", 0) > 0){
            Ion.with(getBaseContext())
                    .load(URL)
                    .setMultipartParameter("id_user", id_user)
                    .setMultipartParameter("token_user", token)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                        }
                    });
        }
    }
}