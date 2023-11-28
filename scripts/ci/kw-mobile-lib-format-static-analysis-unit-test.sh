#!/bin/bash

./gradlew --profile --continue --parallel clean spotlessCheck -Denable.spotless=true
exitStatus=$?
if [ $exitStatus -ne 0 ]; then
    echo -e "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "One or more backend files are not properly formatted."
    echo "To fix, run: ./gradlew spotlessApply -Denable.spotless=true"
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit $exitStatus
fi

./gradlew --profile --continue --parallel clean testClasses -Dbuild.errorprone=true
exitStatus=$?
if [ $exitStatus -ne 0 ]; then
    echo -e "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "One or more errorprone violations found; fix to continue."
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit $exitStatus
fi

./gradlew --profile --continue --parallel clean testClasses -Dbuild.checker=true
exitStatus=$?
if [ $exitStatus -ne 0 ]; then
    echo -e "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "One or more checkerframework violations found; fix to continue."
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit $exitStatus
fi