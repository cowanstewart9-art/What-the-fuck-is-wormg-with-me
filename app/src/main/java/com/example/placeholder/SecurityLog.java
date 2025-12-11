package com.example.placeholder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SecurityLog {
    private static final List<String> logs = new ArrayList<>();
    private static final int MAX_LOGS = 1000;
    private static OnLogUpdateListener listener;

    public interface OnLogUpdateListener {
        void onLogUpdated();
    }

    public static synchronized void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
        logs.add(0, "[" + timestamp + "] " + message);
        if (logs.size() > MAX_LOGS) {
            logs.remove(logs.size() - 1);
        }
        if (listener != null) {
            listener.onLogUpdated();
        }
    }

    public static synchronized List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public static void setListener(OnLogUpdateListener l) {
        listener = l;
    }

    public static void removeListener() {
        listener = null;
    }
}
