package example.amc.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import example.amc.user.LoginActivity;
import example.amc.R;

/**
 * Created by KO on 2017-09-22.
 */

public class FirebaseMessagingServicE extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("remoteMessage::"+remoteMessage);
        System.out.println("getMessageType::"+remoteMessage.getMessageType());
        System.out.println("getNotification()::"+remoteMessage.getNotification());
        System.out.println(remoteMessage.getData().toString());
        System.out.println(remoteMessage.getMessageId());

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"TAG");
        wakelock.acquire(3000);
        //wakelock.release();
        Map<String,String> map = new HashMap<>();
        map.put("message",remoteMessage.getData().get("message"));
        map.put("subject",remoteMessage.getData().get("subject"));


        if (remoteMessage.getData().size() > 0){
            System.out.println("size가 0보다 큼");
            sendPushNotification(map);
        }
        if (remoteMessage.getNotification() != null){
            System.out.println("노티피케이션이 널이 아니다");
            //sendPushNotification(remoteMessage.getNotification().getBody());
        }
//        if (remoteMessage.getNotification() == null){
//            System.out.println("노티피케이션이 널이다");
//            sendPushNotification("널이 온다~~");
//        }

    }

    private void sendPushNotification(Map<String,String> map) {
        System.out.println("센드 푸쉬 노티피케이션 received message : " + map.get("message"));
        Intent intent = new Intent(this, LoginActivity.class);
        System.out.println("check check :: 111111111");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        System.out.println("check check :: 2222222222");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        System.out.println("check check :: 3333333333");
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.kp).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher) )
                .setContentTitle(map.get("subject"))
                .setContentText(map.get("message"))
                .setAutoCancel(true)
                .setVibrate(new long[]{500,1000})
                .setLights(123, 1000, 2323)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);
        System.out.println("check check :: 4444444");

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(notificationBuilder);
        style.bigText(map.get("message")).setBigContentTitle(map.get("subject"));

        // 알람 클릭시 MainActivity를 화면에 띄운다.
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext()
//                , 0
//                , intent
//                , Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//        builder.setContentIntent(pIntent);
//        manager.notify(1, builder.build());


//        출처: http://androphil.tistory.com/368 [소림사의 홍반장!]

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
