# Jules Security Monitor & Tools

A comprehensive security suite containing an Android Traffic Monitor and Python-based utilities for safe environment testing.

## Android Application

### Features
*   **🛡️ Overlay Detection (Anti-Tapjacking):** Detects and blocks inputs when an overlay is obscuring the screen.
*   **📶 Network Traffic Monitoring:** Real-time background tracking of upload/download rates and network changes (WiFi/Mobile/VPN).
*   **🤖 Heuristic AI:** Analyzes traffic patterns to detect anomalies like rapid switching or massive data spikes.
*   **Run on Boot:** Automatically restarts monitoring after device reboot.

### Installation
1.  Download `JulesSecurity.apk` from the **GitHub Releases** page.
2.  Install on your Android device (Android 5.0+, optimized for Android 14).

## Python Utilities

The repository includes standalone Python tools located in `python_scripts/`.

### Tic-Tac-Toe (AI)
A command-line game with a Minimax-based AI, suitable for testing terminal capabilities (e.g., in Termux).

**Run on Termux:**
```bash
./termux_run.sh
```

**Run Locally:**
```bash
python3 python_scripts/tictactoe.py
```

## Development

### Android Build
```bash
./gradlew assembleDebug
```
*Note: Requires JDK 17 and Android SDK.*

### Project Structure
*   `app/`: Android Studio project files.
*   `python_scripts/`: Python tools and tests.
*   `.github/workflows/`: CI/CD pipelines for automatic building and releasing.
