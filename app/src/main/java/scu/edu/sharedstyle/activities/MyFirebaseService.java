package scu.edu.sharedstyle.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import scu.edu.sharedstyle.R;
import scu.edu.sharedstyle.model.GlideApp;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MyFirebaseService extends FirebaseMessagingService {
    Bitmap bitmap;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    public MyFirebaseService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String imageUri = remoteMessage.getData().get("image");
        bitmap = getBitmapfromUrl(imageUri);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        // Check if message contains a notification payload.
        if (notification != null) {
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d("TAG", "Message Notification getTitle: " + remoteMessage.getNotification().getTitle());
            Log.d("TAG", "Message Notification Tag: " + remoteMessage.getNotification().getTag());
            Log.d("TAG", "Message Notification Icon: " + remoteMessage.getNotification().getIcon());
            Log.d("TAG", "Message Notification BodyLocalizationKey: " + remoteMessage.getNotification().getBodyLocalizationKey());
            Log.d("TAG", "Message Notification ClickAction: " + remoteMessage.getNotification().getClickAction());
            Log.d("TAG", "Message Notification Color: " + remoteMessage.getNotification().getColor());
            Log.d("TAG", "Message Notification link: " + remoteMessage.getNotification().getLink());
            Log.d("TAG", "Message Notification ClickAction: " + remoteMessage.getNotification().getClass());
            for (String key : remoteMessage.getData().keySet()) {
                Log.d("TAG", "Message Notification key: " + key);
                Log.d("TAG", "Message Notification key: " + remoteMessage.getData().get("image"));
            }
            Log.d("TAG", "Message Notification ClickAction: " + remoteMessage.getData());

        }

        try {
            sendNotification(remoteMessage, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }



    private void sendNotification(RemoteMessage remoteMessage, Bitmap image) throws IOException {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Intent intent = new Intent(this, MainActivity.class);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //8.0 以后需要加上channelId 才能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        //设置TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification1 = new NotificationCompat.Builder(MyFirebaseService.this, "default")
                .setLargeIcon(image)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(ContextCompat.getColor(MyFirebaseService.this, R.color.colordarkpurple))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        manager.notify(1, notification1);
    }


    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        Bitmap bitmap=null;
        try {
            bitmap = GlideApp
                    .with(this)
                    .asBitmap()
                    .load(storageRef)
                    .submit()
                    .get();
        }catch(Exception e){

        }
        return bitmap;
    }
    }
