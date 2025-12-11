package com.example.placeholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity implements View.OnTouchListener {

    private TextView logView;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Programmatic UI
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Convert DP to Pixels
        float density = getResources().getDisplayMetrics().density;
        int padding = (int) (16 * density);

        layout.setPadding(padding, padding, padding, padding);
        layout.setBackgroundColor(Color.BLACK);

        TextView title = new TextView(this);
        title.setText("Jules Traffic Monitor & Security");
        title.setTextSize(24);
        title.setTextColor(Color.GREEN);
        layout.addView(title);

        TextView status = new TextView(this);
        status.setText("Status: Monitoring Active");
        status.setTextColor(Color.WHITE);
        status.setPadding(0, 16, 0, 16);
        layout.addView(status);

        TextView logLabel = new TextView(this);
        logLabel.setText("Security Logs:");
        logLabel.setTextColor(Color.YELLOW);
        layout.addView(logLabel);

        ScrollView scrollView = new ScrollView(this);
        logView = new TextView(this);
        logView.setTextColor(Color.LTGRAY);
        logView.setTextSize(14);
        scrollView.addView(logView);

        // Make scroll view take remaining space
        LinearLayout.LayoutParams scrollParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.addView(scrollView, scrollParams);

        // Set content view
        setContentView(layout);

        // Overlay Detection
        // We set the touch listener on the root view
        layout.setOnTouchListener(this);

        // Start Service
        startForegroundServiceCompat();

        // Request Notifications (Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Update logs
        SecurityLog.setListener(new SecurityLog.OnLogUpdateListener() {
            @Override
            public void onLogUpdated() {
                updateLogs();
            }
        });
        updateLogs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SecurityLog.removeListener();
    }

    private void startForegroundServiceCompat() {
        Intent serviceIntent = new Intent(this, BackgroundMonitorService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void updateLogs() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                for (String log : SecurityLog.getLogs()) {
                    sb.append(log).append("\n");
                }
                logView.setText(sb.toString());
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Overlay Detection
        int flags = event.getFlags();
        // FLAG_WINDOW_IS_OBSCURED = 1
        // FLAG_WINDOW_IS_PARTIALLY_OBSCURED = 2 (API 31+)

        boolean obscured = (flags & MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0;

        if (obscured) {
            SecurityLog.log("SECURITY ALERT: Tapjacking attempt detected! Overlay is blocking touches.");
            // We might want to block the action here or show a warning dialog
            // returning true would consume the event, effectively blocking it
            return true; // Block touch
        }

        return false; // Let it propagate if safe
    }
}
