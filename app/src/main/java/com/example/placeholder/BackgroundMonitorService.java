package com.example.placeholder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.app.ActivityManager;
import java.util.List;

public class BackgroundMonitorService extends Service {

    private static final String CHANNEL_ID = "monitor_channel";
    private static final int NOTIFICATION_ID = 1;
    private Handler handler;
    private Runnable runnable;
    private static final long CHECK_INTERVAL = 5000; // 5 seconds
    private static final long IDLE_THRESHOLD = 30000; // 30 seconds
    private boolean isIdleNotificationSent = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification("Monitoring", "Background monitor is running"));
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                checkIdleStatus();
                checkBackgroundProcesses();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };
        handler.post(runnable);
    }

    private void checkIdleStatus() {
        long lastInteraction = MainActivity.lastInteractionTime;
        if (lastInteraction > 0 && (System.currentTimeMillis() - lastInteraction) > IDLE_THRESHOLD) {
            if (!isIdleNotificationSent) {
                sendNotification("Idle Warning", "App has been running without user input for a while.");
                isIdleNotificationSent = true;
            }
        } else {
            isIdleNotificationSent = false;
        }
    }

    private void checkBackgroundProcesses() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            // NOTE: On Android 5.1+, this returns only the current app process.
            // This logic is a placeholder for demonstration or system-level apps.
            List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
            if (processes != null) {
                for (ActivityManager.RunningAppProcessInfo process : processes) {
                    if (process.processName.contains("malicious") || process.processName.contains("spyware")) {
                        sendNotification("Security Alert", "Suspicious process detected: " + process.processName);
                    }
                }
            }
        }
    }

    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), createNotification(title, message));
    }

    private Notification createNotification(String title, String message) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        return builder
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .build();
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
