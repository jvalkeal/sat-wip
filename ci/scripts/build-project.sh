#!/bin/bash
set -e

source $(dirname $0)/common.sh

pushd git-repo > /dev/null
echo $ARTIFACTORY_USERNAME
./gradlew clean build
popd > /dev/null
