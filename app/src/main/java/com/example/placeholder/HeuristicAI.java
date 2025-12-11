package com.example.placeholder;

public class HeuristicAI {

    private long lastNetworkChangeTime = 0;
    private int rapidChangeCount = 0;
    private static final long THRESHOLD_MS = 2000;

    public void analyzeNetworkEvent(String eventType) {
        long now = System.currentTimeMillis();
        if (now - lastNetworkChangeTime < THRESHOLD_MS) {
            rapidChangeCount++;
        } else {
            rapidChangeCount = 0;
        }
        lastNetworkChangeTime = now;

        if (rapidChangeCount >= 3) {
            SecurityLog.log("AI ALERT: Suspicious rapid network switching detected!");
            rapidChangeCount = 0; // Reset
        }
    }

    public void analyzeTraffic(long bytesRx, long bytesTx) {
        // Simple heuristic: If downloading huge amount quickly (mock check)
        if (bytesRx > 50 * 1024 * 1024) { // 50MB
             SecurityLog.log("AI WARNING: High background data usage detected: " + (bytesRx / 1024 / 1024) + "MB");
        }
    }
}
