# Jules Security Monitor

A robust Android Security Application designed to monitor network traffic, detect anomalies, and prevent overlay attacks.

## Features

### 🛡️ Overlay Detection (Anti-Tapjacking)
- Detects if a malicious app is drawing an overlay on top of the security monitor.
- Blocks touch inputs when an overlay is detected (`FLAG_WINDOW_IS_OBSCURED`).
- Logs the attempt in the security log.

### 📶 Network Traffic Monitoring
- **Real-time Monitoring**: Tracks background data usage (Upload/Download).
- **Connectivity Analysis**: Monitors network type changes (WiFi/Mobile/VPN).
- **Heuristic AI**: Detects suspicious patterns like rapid network switching or unusual data spikes.
- **Foregound Service**: Runs continuously in the background with a persistent notification.

### 📝 Security Logging
- Thread-safe in-memory logging.
- Live programmatic UI display of security events.

## Build & Installation

### Android 14 Ready
This app is fully compliant with Android 14 (API 34) Foreground Service requirements, utilizing the `specialUse` service type.

### Local Build
```bash
./gradlew assembleDebug
```
The APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.

### CI/CD
This repository is configured with GitHub Actions. Every push to the `restore-security-app` branch triggers a build that generates the APK artifact.
