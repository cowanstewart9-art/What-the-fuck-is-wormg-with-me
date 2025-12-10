package com.example.placeholder;

import android.app.AppOpsManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static volatile long lastInteractionTime = 0;
    private static final String CHANNEL_ID = "monitor_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastInteractionTime = System.currentTimeMillis();

        createNotificationChannel();
        requestNotificationPermission();
        checkUsageStatsPermission();

        Intent serviceIntent = new Intent(this, BackgroundMonitorService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void checkUsageStatsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());

            if (mode != AppOpsManager.MODE_ALLOWED) {
                Toast.makeText(this, "Please grant usage access to monitor apps", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        lastInteractionTime = System.currentTimeMillis();
        if (event.getAction() == MotionEvent.ACTION_DOWN && (event.getFlags() & MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0) {
            Toast.makeText(this, "Warning: Overlay detected!", Toast.LENGTH_LONG).show();
            sendOverlayNotification();
        }
        return super.dispatchTouchEvent(event);
    }

    private void sendOverlayNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        Notification notification = builder
                .setContentTitle("Security Warning")
                .setContentText("An overlay was detected over the application screen!")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .build();

        notificationManager.notify(999, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Monitor Channel";
            String description = "Channel for background monitoring alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
