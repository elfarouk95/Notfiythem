package com.example.alfaroukomar.notfiythem;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class notify_them extends IntentService {

    Firebase fm ;
    public notify_them() {

        super("notify_them");
        fm=new Firebase("https://notify-them.firebaseio.com");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        fm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  addNotification("حالة جديدة محتاجة مساعدة");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        fm.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addNotification("حالة جديدة محتاجة مساعدة");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void addNotification(String x) {



        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(" Notify-them Notifications")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(x);

        Intent notificationIntent = new Intent(this,News.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
