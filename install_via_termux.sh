#!/bin/bash

# Define the repository and APK name
REPO_URL="https://github.com/cowanstewart9-art/What-the-fuck-is-wormg-with-me"
APK_NAME="JulesSecurity.apk"
DOWNLOAD_URL="$REPO_URL/releases/latest/download/$APK_NAME"

echo "=== Jules Security Installer for Termux ==="

# 1. Check/Install wget
if ! command -v wget &> /dev/null; then
    echo "Installing wget..."
    pkg install wget -y
fi

# 2. Download the APK
echo "Downloading $APK_NAME..."
wget -q --show-progress -O "$APK_NAME" "$DOWNLOAD_URL"

if [ -f "$APK_NAME" ]; then
    echo "Download complete."

    # 3. Prompt to install
    echo "Launching Android Installer..."
    echo "Please approve the installation on your screen."

    # termux-open is a Termux-specific utility to open files with the default Android app
    if command -v termux-open &> /dev/null; then
        termux-open "$APK_NAME"
    else
        echo "Error: 'termux-open' not found. Are you running this in Termux?"
        echo "You can manually open $APK_NAME from your file manager."
    fi
else
    echo "Error: Download failed."
    exit 1
fi
