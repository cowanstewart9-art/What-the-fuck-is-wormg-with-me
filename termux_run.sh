#!/bin/bash

# Check if Python is installed
if ! command -v python3 &> /dev/null; then
    echo "Python 3 is not installed."
    echo "Please install it by running: pkg install python"

fi

echo "Starting Tic Tac Toe for Termux..."
python3 python_scripts/tictactoe.py
