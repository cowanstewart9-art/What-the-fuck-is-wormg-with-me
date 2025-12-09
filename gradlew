#!/usr/bin/env bash

#
# Copyright 2015 the original author or authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# For Darwin, add options to specify how the application appears in the dock
# Dock icon and name are automatically set by the gradle launcher based on the name of the directory containing the script.
# For example, if the script is in /Users/user/my-project, then the icon will be chosen from ./.gradle/gradle.icns and the name
# will be "my-project".
if [ "$(uname)" = "Darwin" ]; then
    GRADLE_OPTS="$GRADLE_OPTS \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=.gradle/gradle.icns\""
fi

# Add a semi-colon-separated list of paths to the end of the classpath.
CLASSPATH=""

# Add the gradle wrapper jar to the classpath
# The jar is located in the same directory as this script.
APP_HOME="$(cd `dirname "$0"` && pwd)"
APP_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Add the gradle wrapper jar to the classpath.
if [ -n "$CLASSPATH" ]; then
    CLASSPATH="$APP_JAR:$CLASSPATH"
else
    CLASSPATH="$APP_JAR"
fi

# Set the GRADLE_HOME.
if [ -z "$GRADLE_HOME" ]; then
    GRADLE_HOME="$APP_HOME"
fi

# Set the JAVA_HOME.
if [ -z "$JAVA_HOME" ]; then
    # Attempt to find JAVA_HOME from javac
    JAVAC_EXE=`which javac 2>/dev/null`
    if [ -n "$JAVAC_EXE" ]; then
        if [ "$(uname)" = "Darwin" ]; then
            JAVA_HOME=`/usr/libexec/java_home`
            export JAVA_HOME
        elif [ "$(uname)" = "Linux" ]; then
            JAVA_HOME=`readlink -f \`which javac\` | sed "s:/bin/javac::"`
            export JAVA_HOME
        fi
    fi
fi

# Set the JVM options.
if [ -n "$JAVA_OPTS" ]; then
    GRADLE_OPTS="$JAVA_OPTS $GRADLE_OPTS"
fi

# Launch the Gradle command.
exec java $DEFAULT_JVM_OPTS $GRADLE_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
