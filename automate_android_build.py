import os
import subprocess
import sys

def run_command(command_args):
    """Executes a command provided as a list of arguments."""
    cmd_str = " ".join(command_args)
    print(f"Executing: {cmd_str}")
    try:
        # shell=False is the default, ensuring arguments are passed safely
        subprocess.check_call(command_args)
        print("Success.")
    except subprocess.CalledProcessError as e:
        print(f"Error executing command: {e}")
        sys.exit(1)

def automate_workflow(commit_message):
    print("Initializing Android Automation Workflow...")

    # Check if git is available
    run_command(["git", "--version"])

    # Add changes
    print("Adding changes to git...")
    run_command(["git", "add", "."])

    # Commit changes
    print(f"Committing with message: '{commit_message}'...")
    try:
        run_command(["git", "commit", "-m", commit_message])
    except SystemExit:
        # subprocess.check_call exits via sys.exit(1) in run_command, catch here?
        # Actually run_command calls sys.exit(1).
        # We might want to allow empty commits to fail gracefully if needed,
        # but check_call raises CalledProcessError.
        pass
    except subprocess.CalledProcessError:
         print("Commit failed (perhaps nothing to commit?). Continuing...")


    # Push changes
    print("Pushing to remote to trigger GitHub Action...")
    run_command(["git", "push", "origin", "HEAD"])

    print("-" * 30)
    print("Workflow Submitted!")
    print("GitHub Actions should now be building your APK.")
    print("Check the 'Actions' tab in your repository.")
    print("-" * 30)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        # Join all arguments to form the message, handling spaces correctly
        message = " ".join(sys.argv[1:])
    else:
        message = "Automated build submission"

    print("Note: This script assumes you have git configured and remote set up.")
    automate_workflow(message)
