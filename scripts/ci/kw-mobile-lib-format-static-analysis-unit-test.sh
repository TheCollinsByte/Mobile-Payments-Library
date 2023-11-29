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

./gradlew --profile --continue --parallel --max-workers=8 clean check "$@"
exitStatus=$?

# Make unit test reports available for Github Actions to pickup
DEST="build/ci/backend-format-static-analysis-unit-test"
rm -rf $DEST
mkdir -p $DEST
find . -type f -regex ".*/build/test-results/.*xml" -exec cp --target-directory=$DEST {} +

if [ $exitStatus -ne 0 ]; then
    echo -e "\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "One or more backend unit tests failed; fix to continue."
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit $exitStatus
fi