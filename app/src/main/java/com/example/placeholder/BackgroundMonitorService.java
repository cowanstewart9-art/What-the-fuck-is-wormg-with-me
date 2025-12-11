package com.example.placeholder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.TrafficStats;
import android.os.Build;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class BackgroundMonitorService extends Service {

    private static final String CHANNEL_ID = "SecurityChannel";
    private HeuristicAI ai;
    private Handler handler;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private long lastRxBytes = 0;
    private long lastTxBytes = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        ai = new HeuristicAI();
        handler = new Handler(Looper.getMainLooper());
        SecurityLog.log("Service: Security Monitor Starting...");

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(1, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(1, createNotification());
        }

        setupNetworkMonitoring();
        startTrafficMonitoring();
    }

    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Security Monitor", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        return builder.setContentTitle("Security Monitor Active")
                .setContentText("Monitoring network and background activity...")
                .setSmallIcon(android.R.drawable.ic_lock_idle_lock) // Using system icon
                .build();
    }

    private void setupNetworkMonitoring() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return;

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                SecurityLog.log("Network: Connected");
                ai.analyzeNetworkEvent("CONNECTED");
            }

            @Override
            public void onLost(Network network) {
                SecurityLog.log("Network: Disconnected");
                ai.analyzeNetworkEvent("DISCONNECTED");
            }

            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                 if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                     SecurityLog.log("Network: VPN Detected active!");
                 }
            }
        };

        // Monitor all networks
        NetworkRequest request = new NetworkRequest.Builder().build();
        connectivityManager.registerNetworkCallback(request, networkCallback);
    }

    private void startTrafficMonitoring() {
        // Initial reading
        lastRxBytes = TrafficStats.getTotalRxBytes();
        lastTxBytes = TrafficStats.getTotalTxBytes();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTraffic();
                handler.postDelayed(this, 10000); // Check every 10 seconds
            }
        }, 10000);
    }

    private void checkTraffic() {
        long currentRx = TrafficStats.getTotalRxBytes();
        long currentTx = TrafficStats.getTotalTxBytes();

        long deltaRx = currentRx - lastRxBytes;
        long deltaTx = currentTx - lastTxBytes;

        if (deltaRx > 0 || deltaTx > 0) {
             // Only log significant traffic to avoid spam
             if (deltaRx > 1024 * 1024) { // 1MB
                 SecurityLog.log("Traffic: High download detected (" + (deltaRx/1024) + " KB)");
             }
             ai.analyzeTraffic(deltaRx, deltaTx);
        }

        lastRxBytes = currentRx;
        lastTxBytes = currentTx;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
        SecurityLog.log("Service: Security Monitor Stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
