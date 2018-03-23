package br.com.rlsystem.chat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {

            if (remoteMessage.getData().get("action").equals("NEW_MESSAGE")) {
                int id_user = Integer.parseInt(remoteMessage.getData().get("id_user"));
                int contact_user = Integer.parseInt(remoteMessage.getData().get("contact_user"));
                String mensagem = remoteMessage.getData().get("message");
                String data = remoteMessage.getData().get("data");

                sendNotification(mensagem, id_user);

                SQLiteHelper db = new SQLiteHelper(getBaseContext());
                db.insert_message(id_user, contact_user, mensagem, data);

                Intent it = new Intent("REFRESH");
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);
            } else if (remoteMessage.getData().get("action").equals("NEW_CONTACT")) {
                int id_user = Integer.parseInt(remoteMessage.getData().get("id_user"));
                int contact_user = Integer.parseInt(remoteMessage.getData().get("contact_user"));
                String foto_usuario = remoteMessage.getData().get("photo_user");
                String nome_usuario = remoteMessage.getData().get("nome_user");

                SQLiteHelper db = new SQLiteHelper(getBaseContext());
                db.save_contact(contact_user, id_user, nome_usuario, foto_usuario);

                Intent it = new Intent("CONTACT");
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);
            }
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        /*
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

    }
    private void sendNotification(String messageBody, int contact_user) {
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("contact_user", contact_user);
        intent.putExtra("name_contact", "NOME PADRAO");
        intent.putExtra("photo_contact", "http://192.168.25.16/chat/photos/Rafael_Silva_991.png");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.no_photo)
                .setContentTitle("Nova Mensagem")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Random r = new Random();
        notificationManager.notify(r.nextInt(), notificationBuilder.build());

    }
}